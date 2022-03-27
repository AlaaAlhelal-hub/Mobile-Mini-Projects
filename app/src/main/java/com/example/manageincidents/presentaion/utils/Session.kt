package com.example.manageincidents.presentaion.utils

import com.example.manageincidents.domain.models.User

class Session(private val preferenceUtil: PreferenceUtil, private val localeUtil: LocaleUtil) {

    companion object {
        private const val USER_DATA_KEY = "USER_DATA_KEY"
        private const val TOKEN = "TOKEN"
        private const val DEVICE_GCM_TOKEN_KEY = "DEVICE_GCM_TOKEN_KEY"

        // Cached objects
        private var mUser: User? = null
        private var mAccessToken: String? = null
    }

    val isArabicLocale: Boolean
        get() = localeUtil.isArabicLocale

    val customeUrl: String?
        get() =
            if (preferenceUtil.getStringValue("customeUrl").isNullOrEmpty())
            {
                null
            }else{
                preferenceUtil.getStringValue("customeUrl").toString()
            }


    val currentLanguage: String
        get() = if (isArabicLocale) {
            "ARABIC"
        } else {
            "ENGLISH"
        }

    var user: User?
        get() {
            if (mUser == null) {
                mUser = preferenceUtil[USER_DATA_KEY, User::class.java] as User
            }

            return mUser
        }
        set(value) {
            mUser = value
            Thread(Runnable { preferenceUtil.putUserDetails(value!!) }).start()
        }

    var accessToken: String? = null
        get() {
            if (mAccessToken == null) {
                mAccessToken = preferenceUtil.getStringValue(
                    TOKEN, null)
            }

            return mAccessToken
        }
        set(value) {
            mAccessToken = value
            Thread(Runnable { preferenceUtil.putStringValue(TOKEN, value) }).start()
            field = value
        }



    val isLoggedIn: Boolean
        get() = user != null

    fun logout() {
        mUser = null
        mAccessToken = null

        preferenceUtil.clear(USER_DATA_KEY)
        preferenceUtil.clear(TOKEN)
    }
}
