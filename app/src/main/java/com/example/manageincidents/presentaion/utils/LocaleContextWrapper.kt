package com.example.manageincidents.presentaion.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

class LocaleContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        @TargetApi(Build.VERSION_CODES.N)
        fun wrap(context: Context, newLocale: Locale?): ContextWrapper {
            var context = context
            val res = context.resources
            val configuration = res.configuration
            if (Build.VERSION.SDK_INT >= 25) {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                context = context.createConfigurationContext(configuration)
            } else if (Build.VERSION.SDK_INT > 17) {
                configuration.setLocale(newLocale)
                context = context.createConfigurationContext(configuration)
            } else {
                configuration.locale = newLocale
                res.updateConfiguration(configuration, res.displayMetrics)
            }
            return ContextWrapper(context)
        }
    }
}