package com.app.qrcodeapplication.ui.scan

import android.app.AlertDialog
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.extensions.doOnLayout
import com.app.qrcodeapplication.extensions.rect
import com.app.qrcodeapplication.extensions.size
import com.app.qrcodeapplication.model.system.QRCodeFrameProcessor
import com.app.qrcodeapplication.model.system.QRCodeResult
import com.app.qrcodeapplication.presentation.scan.ScanCodePresenter
import com.app.qrcodeapplication.presentation.scan.ScanCodeView
import com.app.qrcodeapplication.ui.MainActivity
import com.app.qrcodeapplication.ui.global.BaseFragment
import com.app.qrcodeapplication.ui.global.showCheckData
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.error.CameraErrorCallback
import io.fotoapparat.exception.camera.CameraException
import io.fotoapparat.selector.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_scan.*

class ScanFragment : BaseFragment(), ScanCodeView {
    @InjectPresenter(type = PresenterType.GLOBAL, tag = PRESENTER_TAG)
    lateinit var presenter: ScanCodePresenter

    @ProvidePresenter(type = PresenterType.GLOBAL, tag = PRESENTER_TAG)
    fun provideScanCodePresenter() = ScanCodePresenter()


    private lateinit var fotoapparat: Fotoapparat
    private var permissionsGranted: Boolean = false
    private lateinit var permissionsDelegate: PermissionsDelegate
    private var currentState: State = State.Initialization
    private var compositeDisposable = CompositeDisposable()
    private var barcodeSubscription: Disposable? = null

    lateinit var QRCodeFrameProcessor: QRCodeFrameProcessor

    private val cameraConfiguration = CameraConfiguration(
        previewResolution = firstAvailable(
            standardRatio(highestResolution())
        ),
        previewFpsRange = highestFps(),
        flashMode = off(),
        focusMode = firstAvailable(
            continuousFocusPicture(),
            autoFocus()
        ),
        frameProcessor = {
            QRCodeFrameProcessor.process(it)
        }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).supportActionBar?.setTitle(getString(R.string.scanning))
        QRCodeFrameProcessor = QRCodeFrameProcessor()



        currentState = State.Initialization

        permissionsDelegate = PermissionsDelegate(this)
        permissionsGranted = permissionsDelegate.hasCameraPermission()

        if (!permissionsGranted) {
            permissionsDelegate.requestCameraPermission()
        }

        activity?.let {
            fotoapparat = Fotoapparat(
                context = it,
                view = cameraView,
                focusView = focusView,
                cameraConfiguration = cameraConfiguration,
                cameraErrorCallback = cameraErrorCallback
            )
        }

        setupBarcodeProcessing()

        overlayView.apply {
            overlayColor = ContextCompat.getColor(context, R.color.overlayColor)
        }
    }

    private fun setupBarcodeProcessing() {
        cameraView.doOnLayout { QRCodeFrameProcessor.previewSize = cameraView.size }
        cardImageView.doOnLayout {
            overlayView.drawRect = RectF(it.rect)
            QRCodeFrameProcessor.previewRect = it.rect
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionsGranted) {
            fotoapparat.start()
            enableFrameProcessing()
        }
    }

    override fun onPause() {
        super.onPause()
        if (permissionsGranted) {
            fotoapparat.stop()
        }
        disableFrameProcessing()
        compositeDisposable.dispose()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
            permissionsGranted = true
            fotoapparat.start()
            enableFrameProcessing()
        } else {
            changeState(State.NoCameraPermissions)
        }
    }

    private fun enableFrameProcessing() {
        QRCodeFrameProcessor.enabled = true

        val disposable = barcodeSubscription
        if (disposable == null || disposable.isDisposed) {
            barcodeSubscription = QRCodeFrameProcessor.observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onBarcodeResult(it)
                }
            barcodeSubscription?.let {
                if (compositeDisposable.isDisposed) {
                    compositeDisposable = CompositeDisposable()
                }
                compositeDisposable.add(it)
            }
        }
    }

    private fun disableFrameProcessing() {
        QRCodeFrameProcessor.enabled = false
    }

    private fun onBarcodeResult(QRCodeResult: QRCodeResult) {
        when (QRCodeResult) {
            is QRCodeResult.Success -> {
                changeState(State.BarcodeDetected(QRCodeResult))
                barcodeSubscription?.dispose()
            }
        }
    }

    val cameraErrorCallback: CameraErrorCallback = object : CameraErrorCallback {
        override fun invoke(p1: CameraException) {
            showError(p1)
        }
    }

    private fun changeState(state: State) {
        if (state == currentState) {
            return
        }
        currentState = state

        when (state) {
            is State.NoCameraPermissions -> {
                AlertDialog.Builder(context)
                    .setMessage(getString(R.string.camera_permission_error))
                    .setPositiveButton(context?.getString(R.string.ok)) { dialog, which ->
                        dialog.dismiss()
                        permissionsDelegate.requestCameraPermission()
                    }
                    .setNegativeButton(context?.getString(R.string.cancel)) { dialog, which ->
                        dialog.dismiss()
                        activity?.onBackPressed()
                    }
                    .create().show()
            }
            is State.BarcodeDetected -> {
                disableFrameProcessing()
                presenter.check(state.QRCodeResult.code)
            }
        }
    }

    override fun showMessage(check: Check) {
        context?.let {
            showCheckData(it, check) {
                enableFrameProcessing()
            }
        }
    }

    sealed class State {
        object Initialization : State()
        object NoCameraPermissions : State()
        data class BarcodeDetected(var QRCodeResult: QRCodeResult.Success) : State()
    }


    override fun showProgress(progress: Boolean) {
        if(progress) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }
    }

    companion object {
        private const val PRESENTER_TAG = "ScanCodePresenter"
    }
}