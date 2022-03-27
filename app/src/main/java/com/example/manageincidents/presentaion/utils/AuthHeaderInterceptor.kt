package com.example.manageincidents.presentaion.utils

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(private val session: Session) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = session.accessToken
        var request = chain.request()

        request = if (request.header("No-Authentication") == null && token != null) {
            request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else {
            request.newBuilder().build()
        }

        return chain.proceed(request)
    }
}
