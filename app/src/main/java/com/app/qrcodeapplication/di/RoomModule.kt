package com.app.qrcodeapplication.di

import android.arch.persistence.room.Room
import com.app.qrcodeapplication.QRCodeApplication
import com.app.qrcodeapplication.model.data.db.CheckDao
import com.app.qrcodeapplication.model.data.db.QRCodeDatabase
import com.app.qrcodeapplication.model.data.db.QRCodeLocalCache
import com.app.qrcodeapplication.model.data.server.QRCodeApi
import com.app.qrcodeapplication.model.interactor.QRCodeInteractor
import com.app.qrcodeapplication.model.repository.QRCodeRepository
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideLocalDatabase(): QRCodeDatabase {
        return Room.databaseBuilder(
            QRCodeApplication.applicationContext(),
            QRCodeDatabase::class.java, "qrcodeapp.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideUsersDao(appDatabase: QRCodeDatabase): CheckDao {
        return appDatabase.checkDao()
    }

    @Singleton
    @Provides
    fun provideLocalCache(usersDao: CheckDao, ioExecutor: Executor
    ): QRCodeLocalCache {
        return QRCodeLocalCache(usersDao, ioExecutor)
    }

    @Singleton
    @Provides
    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    @Singleton
    @Provides
    fun provideRepository(api: QRCodeApi, cache: QRCodeLocalCache): QRCodeRepository {
        return QRCodeRepository(api, cache)
    }

    @Singleton
    @Provides
    fun provideInteractor(appRepository: QRCodeRepository): QRCodeInteractor {
        return QRCodeInteractor(appRepository)
    }
}