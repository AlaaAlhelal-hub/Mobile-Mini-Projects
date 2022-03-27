package com.example.manageincidents.presentaion.utils

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieInterceptor(val session: Session) : CookieJar {

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            if (cookie.name() == "X-SEC-TK")
                session.accessToken = cookie.value()
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return arrayListOf()
    }

}