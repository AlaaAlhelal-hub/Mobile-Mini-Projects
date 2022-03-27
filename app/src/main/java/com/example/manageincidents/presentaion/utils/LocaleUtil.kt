package com.example.manageincidents.presentaion.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

class LocaleUtil(private val mContext: Context) {
    fun swapLocale() {
        savePreferenceAppLanguage(if (isArabicLocale) Language.ENGLISH.toString() else Language.ARABIC.toString())
        initAppLocale()
    }

    val isArabicLocale: Boolean
        get() = if (preferenceAppLanguage == null) {
            getCurrentConfigLocale(mContext) == Language.ARABIC
        } else {
            preferenceAppLanguage == Language.ARABIC
        }

    enum class Language {
        ARABIC, ENGLISH
    }

    val locale: String
        get() {
            return if (isArabicLocale) {
                "ar"
            } else {
                "en"
            }
        }

    private fun getCurrentConfigLocale(context: Context): Language {
        val defaultLocale = context.resources.configuration.locale.language
        return if (defaultLocale.contains("ar")) {
            Language.ARABIC
        } else {
            Language.ENGLISH
        }
    }

    fun savePreferenceAppLanguage(language: String) {
        val util = PreferenceUtil(mContext)
        util.putStringValue(APP_LANG_KEY, language)
    }

    private val preferenceAppLanguage: Language?
        private get() = getPreferenceLanguage(APP_LANG_KEY)

    private fun getPreferenceLanguage(key: String): Language {
        val util = PreferenceUtil(mContext)
        val currentLanguage = util.getStringValue(key, "")
        return if (currentLanguage == APP_LANG_KEY_AR) {
            Language.ARABIC
        } else if ((currentLanguage == APP_LANG_KEY_EN)) {
            Language.ENGLISH
        } else {
            Language.ENGLISH
        }
    }

    fun initAppLocale() {

        // Set locale with Locale.setDefault will override system locale for this process (Locale.getDefault())
        // And there is not any documented way to retrieve the default locale after reset
        // So it is just enough to set locale for configuration and that will affect getResource method.
        val language = preferenceAppLanguage ?: return
        val locale = createLocale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        mContext.applicationContext.resources.updateConfiguration(
            config,
            mContext.resources.displayMetrics
        )
    }

    private fun createLocale(language: Language): Locale {
        val locale: Locale
        locale = when (language) {
            Language.ARABIC -> Locale("ar", "SA")
            Language.ENGLISH -> Locale("en", "US")
            else -> Locale("en", "US")
        }
        return locale
    }

    companion object {
        val APP_LANG_KEY_EN = Language.ENGLISH.toString()
        val APP_LANG_KEY_AR = Language.ARABIC.toString()
        private const val APP_LANG_KEY = "app_lang"
    }
}
