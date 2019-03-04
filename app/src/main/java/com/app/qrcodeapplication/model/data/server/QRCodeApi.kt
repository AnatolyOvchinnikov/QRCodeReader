package com.app.qrcodeapplication.model.data.server

import com.app.qrcodeapplication.entity.Check
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface QRCodeApi {

    companion object {
        const val API_PATH = "/offline"
        const val SERVER_DATE_FORMAT = "yyyy-MMM-dd hh:mm:ss a ZZ"
    }

    @Headers("Content-Type: application/json")
    @GET("$API_PATH/check")
    fun check(@Query("fiscalNumber") fiscalNumber: String,
                     @Query("fiscalSign") fiscalSign: String,
                     @Query("fiscalDocument") fiscalDocument: String,
                     @Query("date") date: String,
                     @Query("sum") sum: String): Single<Check>
}