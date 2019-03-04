package com.app.qrcodeapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(var mApplication: Context) {

    @Provides
    @Singleton
    fun providesApplication(): Context {
        return mApplication
    }

}