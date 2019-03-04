package com.app.qrcodeapplication.model.repository

import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.model.data.db.QRCodeLocalCache
import com.app.qrcodeapplication.model.data.server.QRCodeApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class QRCodeRepository(val api: QRCodeApi,
                       val cache: QRCodeLocalCache
) {
    fun check(check: Check): Single<Check> {
        cache.insert(check) {

        }
        return api.check(check.fiscalNumber, check.fiscalSign, check.fiscalDocument, check.date, check.sum)
            .map {
                response ->
            cache.insert(response) {

            }
                return@map response
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
//            .subscribe({ response ->
//            }, { error ->
//
//            })
    }

    fun getChecksList(): Single<List<Check>> {
        return cache.getChecksList()
    }
}