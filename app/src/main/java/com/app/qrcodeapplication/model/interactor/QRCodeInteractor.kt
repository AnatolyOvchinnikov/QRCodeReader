package com.app.qrcodeapplication.model.interactor

import android.net.Uri
import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.model.repository.QRCodeRepository
import io.reactivex.Single
import java.text.SimpleDateFormat

class QRCodeInteractor (
        val appRepository: QRCodeRepository
) {
    fun check(text: String): Single<Check> {
//        text = "?t=20190216T1718&s=881.37&fn=9289000100157336&i=56673&fp=3883988655&n=1"
        val uri = Uri.parse("?" + text)

        val fiscalNumber = uri.getQueryParameter("fn")
        val fiscalSign = uri.getQueryParameter("fp")
        val fiscalDocument = uri.getQueryParameter("i")
        val date = uri.getQueryParameter("t")
        var sum = uri.getQueryParameter("s")

        val tf = SimpleDateFormat("yyyyMMdd HHmm"); // 20190303T2144
        val time = date.replace("T", " ")
        val parseTime= tf.parse(time)

        val fDate = SimpleDateFormat("dd.MM.yyyy HH:mm").format(parseTime)

        sum = sum.replace(".", "")

        val tsLong = System.currentTimeMillis() / 1000
        val check = Check(tsLong, fiscalNumber, fiscalSign, fiscalDocument, fDate, sum)
        return appRepository.check(check)
    }

    fun getChecksList(): Single<List<Check>> {
        return appRepository.getChecksList()
    }
}