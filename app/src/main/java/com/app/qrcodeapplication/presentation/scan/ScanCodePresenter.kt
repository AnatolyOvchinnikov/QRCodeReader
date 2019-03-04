package com.app.qrcodeapplication.presentation.scan

import android.annotation.SuppressLint
import com.app.qrcodeapplication.model.interactor.QRCodeInteractor
import com.app.qrcodeapplication.presentation.global.BasePresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ScanCodePresenter: BasePresenter<ScanCodeView>() {

    @Inject
    lateinit var appInteractor: QRCodeInteractor

    private val disposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun check(query: String = "") {
        disposable.clear()

        val queryDisposable = appInteractor.check(query)
            .doOnSubscribe {
                viewState.showProgress(true)
            }
            .subscribe({ response ->
                viewState.showMessage(response)
                viewState.showProgress(false)
            }, { error ->
                viewState.showProgress(false)
                viewState.showError(error)
                Timber.e(error)
            })
        disposable.add(queryDisposable)
    }

    fun onStop() {
        disposable.clear()
    }
}