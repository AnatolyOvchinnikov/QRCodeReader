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
        cache.insert(check)
        return api.check(check.fiscalNumber, check.fiscalSign, check.fiscalDocument, check.date, check.sum)
            .map {
                response ->
                if(response.indexOf(CHECK_DATA) > 0) {
                    check.checkDataHtml = response
                    cache.update(check)
                }

                return@map check
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getChecksList(): Single<List<Check>> {
        return cache.getChecksList()
    }

    companion object {
        private const val CHECK_DATA = "Данные чека"
    }
}