package com.app.qrcodeapplication.ui.global

import com.app.qrcodeapplication.presentation.global.BaseView
import com.arellomobile.mvp.MvpAppCompatFragment
import timber.log.Timber


abstract class BaseFragment : MvpAppCompatFragment(), BaseView {
    fun showError(errorMessage: String) {
        val activity = activity
        if (activity is BaseActivity) {
            activity.showError(errorMessage)
        }
    }

    fun showMessage(message: String) {
        val activity = activity
        if (activity is BaseActivity) {
            activity.showMessage(message)
        }
    }

    override fun showError(error: Throwable) {
        if (error != null) {
            Timber.e(error)
            showError(error.localizedMessage)
        }
    }

    fun showProgressDialog() {
        val activity = activity
        if (activity is BaseActivity) {
            activity.showProgressDialog()
        }
    }

    fun hideProgressDialog() {
        val activity = activity
        if (activity is BaseActivity) {
            activity.hideProgressDialog()
        }
    }

    override fun onDetach() {
        super.onDetach()
        hideProgressDialog()
    }
}