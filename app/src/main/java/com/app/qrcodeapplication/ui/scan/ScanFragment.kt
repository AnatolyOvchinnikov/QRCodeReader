package com.app.qrcodeapplication.ui.scan

import android.graphics.RectF
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.selector.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_scan.*

class ScanFragment : BaseFragment(), ScanCodeView {
    @InjectPresenter(type = PresenterType.GLOBAL, tag = "ScanCodePresenter")
    lateinit var presenter: ScanCodePresenter

    @ProvidePresenter(type = PresenterType.GLOBAL, tag = "ScanCodePresenter")
    fun provideScanCodePresenter() = ScanCodePresenter()


    private lateinit var fotoapparat: Fotoapparat
    private var permissionsGranted: Boolean = false
    private lateinit var permissionsDelegate: PermissionsDelegate
    private var currentState: State = State.Initialization
    private val compositeDisposable = CompositeDisposable()
    private var barcodeSubscription: Disposable? = null
    private var flipYourCardTimer: Disposable? = null

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

        if (permissionsGranted) {
//            changeState(State.WaitingForBarcode)
        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionsDelegate.requestCameraPermission()
//            }
//            presenter.cameraWaitingAuthorization()
        }

        // temp
        presenter.check("t=20190303T2144&s=519.08&fn=9289000100182804&i=53395&fp=610584178&n=1")

        activity?.let {
            fotoapparat = Fotoapparat(
                context = it,
                view = cameraView,
                focusView = focusView,
                cameraConfiguration = cameraConfiguration
//                cameraErrorCallback = cameraErrorCallback
            )
        }

//        setupToolbar()
        setupBarcodeProcessing()

//        overlayView.apply {
//            overlayColor = context.color(R.color.barcodeOverlay)
//            radius = resources.getDimension(R.dimen.barcode_overlay_radius)
//        }
//
//        scanToLoginHintTextView.apply { text = resourceManager.getBrandedString(text) }
//        loginWithEmailButton.setOnClickListener { presenter.loginWithEmail() }
    }

    private fun setupBarcodeProcessing() {
        cameraView.doOnLayout { QRCodeFrameProcessor.previewSize = cameraView.size }
        cardImageView.doOnLayout {
            overlayView.drawRect = RectF(it.rect)
            QRCodeFrameProcessor.previewRect = it.rect
        }
//        barcodeImageView.doOnLayout { barcodeFrameProcessor.barcodeImageSize = barcodeImageView.size }
    }

//    override fun setupToolbar() {
//        (activity as? AppCompatActivity)?.apply {
//            supportActionBar?.hide()
//            setSupportActionBar(barcodeToolbar)
//            supportActionBar?.apply {
//                setDisplayHomeAsUpEnabled(true)
//                setDisplayShowTitleEnabled(false)
//            }
//        }
//        setHasOptionsMenu(true)
//    }

//    override fun onBackPressed() {
////        presenter.onBackPressed()
//        super.onBackPressed()
//    }

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
            permissionsGranted = true
            fotoapparat.start()
//            changeState(State.WaitingForBarcode)
            enableFrameProcessing()
        } else {
//            changeState(State.NoCameraPermissions)
        }
    }

//    @SuppressLint("RestrictedApi")
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        menu.clear()
//        inflater.inflate(R.menu.capture_menu, menu)
//
//        val checkBox = menu.findItem(R.id.capture_menu_flash).actionView
//        val checkableImageButton = checkBox.findViewById<CheckableImageButton>(R.id.flash_button)
//        checkableImageButton.setOnClickListener { v ->
//            (v as? CheckableImageButton)?.apply {
//                isChecked = !isChecked
//                presenter.flashButtonTapped(isChecked)
//
//                fotoapparat.updateConfiguration(
//                    UpdateConfiguration(
//                        flashMode = if (isChecked) {
//                            firstAvailable(torch(), off())
//                        } else {
//                            off()
//                        }
//                    )
//                )
//            }
//        }
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    private fun enableFrameProcessing() {
        QRCodeFrameProcessor.enabled = true

        val disposable = barcodeSubscription
        if (disposable == null || disposable.isDisposed) {
            barcodeSubscription = QRCodeFrameProcessor.observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onBarcodeResult(it) }
            barcodeSubscription?.let { compositeDisposable.add(it) }
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
//            is BarcodeResult.NotReadable -> {
//                changeState(State.BarcodeNotReadable)
//            }
        }
    }

//    val cameraErrorCallback: CameraErrorCallback = object : CameraErrorCallback {
//        override fun invoke(p1: CameraException) {
//            if (activity is MainActivity) {
//                presenter.errorEncountered()
//                (activity as MainActivity).showError(p1)
//            }
//        }
//    }

    private fun changeState(state: State) {
        if (state == currentState) {
            return
        }
        currentState = state

        when (state) {
            is State.BarcodeDetected -> {
                disableFrameProcessing()
                presenter.check(state.QRCodeResult.code)
            }
        }
    }

    override fun showMessage(check: Check) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(check.fiscalNumber)
                .create().show()
        }
    }

    sealed class State {
        object Initialization : State()
        object WaitingForBarcode : State()
        object NoCameraPermissions : State()
        object BarcodeNotReadable : State()
        data class BarcodeDetected(var QRCodeResult: QRCodeResult.Success) : State()
    }


    override fun showProgress(progress: Boolean) {
        if(progress) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }
    }
}