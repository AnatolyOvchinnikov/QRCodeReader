package com.app.qrcodeapplication.model.data.server

import okhttp3.Interceptor
import okhttp3.Response

class ErrorResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val code = response.code()
        when (code) {
            in 500..511 -> throw ServerError()
        }
        return response
    }
}

class ServerError : RuntimeException("Service unavailable")