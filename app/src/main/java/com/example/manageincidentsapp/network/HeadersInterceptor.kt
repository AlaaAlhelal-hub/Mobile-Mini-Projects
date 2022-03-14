package com.example.manageincidentsapp.network

import okhttp3.*
import java.io.IOException

class HeadersInterceptor (private val session: Session): Interceptor, Authenticator {
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${session.mToken}")
            .build()
        return chain.proceed(request)
    }



    @Throws(IOException::class)
    override fun authenticate (route: Route?, response: Response?): Request? {
        var requestAvailable: Request? = null
        try {
            requestAvailable = response?.request()?.newBuilder()
                ?.addHeader("Authorization", "Bearer ${session.mToken}")
                ?.build()
            return requestAvailable
        } catch (ex: Exception) { }
        return requestAvailable
    }
}
