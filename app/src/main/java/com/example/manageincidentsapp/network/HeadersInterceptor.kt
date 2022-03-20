package com.example.manageincidentsapp.network

import okhttp3.*
import java.io.IOException

class HeadersInterceptor (private val prefs: SharedPreferenceManager): Interceptor, Authenticator {
    val token: String?
        get() = prefs.getStringValue("TOKEN", null)

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        if(request.header("No-Authentication") == null){

            if (!request.url().toString().contains("login") && !request.url().toString().contains("verify-otp")) {
                request = token
                    .takeUnless { it.isNullOrEmpty() }
                    ?.let {
                        request.newBuilder()
                            .addHeader("Authorization", "Bearer $it")
                            .build()
                    }
                    ?: request
            }
        }
        return chain.proceed(request)
    }



    @Throws(IOException::class)
    override fun authenticate (route: Route?, response: Response?): Request? {
        var requestAvailable: Request? = null
        try {
            requestAvailable = response?.request()?.newBuilder()
                ?.addHeader("Authorization", "Bearer $token")
                ?.build()
            return requestAvailable
        } catch (ex: Exception) { }
        return requestAvailable
    }
}
