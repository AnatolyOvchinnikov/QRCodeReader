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
    fun insert(check: Check, insertFinished: () -> Unit) {
        ioExecutor.execute {
            usersDao.insert(check)
            insertFinished()
        }
    }

    fun update(check: Check, updateFinished: () -> Unit) {
        ioExecutor.execute {
            usersDao.update(check)
            updateFinished()
        }
    }

    fun getChecksList(): Single<List<Check>> {
        return usersDao.getChecksList()
    }
}