package com.example.guitapp.core

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor() : Interceptor {
    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val originalUrl = original.url
        if (token == null) {
            return chain.proceed(original)
        }
        if (token != null) {
            val tokenHeader = "Bearer " + token
            val requestBuilder = original.newBuilder()
                .header("Authorization", tokenHeader)
                .url(originalUrl)
            original = requestBuilder.build()
        }
        return chain.proceed(original)
    }
}