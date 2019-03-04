package com.app.qrcodeapplication.presentation.history

import android.annotation.SuppressLint
import com.app.qrcodeapplication.model.interactor.QRCodeInteractor
import com.app.qrcodeapplication.presentation.global.BasePresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ScansHistoryPresenter: BasePresenter<ScansHistoryView>() {

    @Inject
    lateinit var appInteractor: QRCodeInteractor

    private val disposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun loadList() {
        disposable.clear()
        val queryDisposable = appInteractor.getChecksList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                viewState.showProgress(true)
            }
            .subscribe({ response ->
                viewState.loadItems(response)
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