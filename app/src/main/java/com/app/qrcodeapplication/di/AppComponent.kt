package com.app.qrcodeapplication.di

import com.app.qrcodeapplication.presentation.scan.ScanCodePresenter
import com.app.qrcodeapplication.presentation.history.ScansHistoryPresenter
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(RoomModule::class, NetworkModule::class))
interface AppComponent {

    fun inject(presenter: ScanCodePresenter)
    fun inject(presenter: ScansHistoryPresenter)
}