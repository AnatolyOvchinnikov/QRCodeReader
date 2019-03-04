package com.app.qrcodeapplication.ui.global

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Toast
import com.app.qrcodeapplication.QRCodeApplication
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.presentation.global.BaseView
import com.arellomobile.mvp.MvpAppCompatActivity
import timber.log.Timber
import java.lang.ref.WeakReference


abstract class BaseActivity : MvpAppCompatActivity(), BaseView {
    private var previousToastWeakReference: WeakReference<Toast>? = null
    private var progressDialog: ProgressDialog? = null

    override fun showError(error: Throwable) {
        Timber.e(error)
        if (error != null) {
            showError(error.localizedMessage)
        }
    }

    fun showError(errorMessage: String) {
        showMessage(errorMessage)
    }

    fun showMessage(message: String) {
        if (message.isBlank()) {
            return
        }

        val previous = if (previousToastWeakReference != null) previousToastWeakReference?.get() else null
        if (previous != null) {
            previous.cancel()
        }

        val toastText = message
        val toast = Toast.makeText(QRCodeApplication.applicationContext(), toastText, Toast.LENGTH_SHORT)
        toast.show()

        previousToastWeakReference = WeakReference<Toast>(toast)
    }

    fun showProgressDialog() {
        if (progressDialog == null || progressDialog?.window == null || progressDialog?.isShowing == false) {
            progressDialog = ProgressDialog(this)
        }
        try {
            if (progressDialog?.isShowing == false) {
                progressDialog?.show()
            }
        } catch (e: WindowManager.BadTokenException) {

        }

        progressDialog?.setCancelable(true)
        progressDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog?.setContentView(R.layout.progressdialog_layout)
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog?.window != null) {
            progressDialog?.dismiss()
        }
        progressDialog = null
    }
}