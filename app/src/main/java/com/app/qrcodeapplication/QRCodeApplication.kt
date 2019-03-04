package com.app.qrcodeapplication

import android.app.Application
import android.content.Context

class QRCodeApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: QRCodeApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
    }

}