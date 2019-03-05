package com.app.qrcodeapplication.model.data.db

import com.app.qrcodeapplication.entity.Check
import io.reactivex.Single
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class QRCodeLocalCache @Inject constructor(val usersDao: CheckDao,
                                                val ioExecutor: Executor
) {
    fun insert(check: Check) {
        ioExecutor.execute {
            usersDao.insert(check)
        }
    }

    fun update(check: Check) {
        ioExecutor.execute {
            usersDao.update(check)
        }
    }

    fun getChecksList(): Single<List<Check>> {
        return usersDao.getChecksList()
    }
}