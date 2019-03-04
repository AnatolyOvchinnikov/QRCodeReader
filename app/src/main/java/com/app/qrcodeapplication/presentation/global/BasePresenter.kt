package com.app.qrcodeapplication.presentation.global

import com.app.qrcodeapplication.di.DaggerAppComponent
import com.app.qrcodeapplication.di.RoomModule
import com.app.qrcodeapplication.presentation.history.ScansHistoryPresenter
import com.app.qrcodeapplication.presentation.scan.ScanCodePresenter
import com.arellomobile.mvp.MvpPresenter

open class BasePresenter<V : BaseView> : MvpPresenter<V>() {

    private var injector = DaggerAppComponent.builder()
        .roomModule(RoomModule())
        .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is ScanCodePresenter -> injector.inject(this)
            is ScansHistoryPresenter -> injector.inject(this)
        }
    }
}