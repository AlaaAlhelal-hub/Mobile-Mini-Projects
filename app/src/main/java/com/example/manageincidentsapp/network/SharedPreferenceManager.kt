package com.example.manageincidentsapp.network

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager constructor(val sharedPreferences: SharedPreferences) {


    companion object {
        const val KEY_TOKEN = "TOKEN"

        fun getInstance(context: Context): SharedPreferenceManager {
            return SharedPreferenceManager(android.preference.PreferenceManager.getDefaultSharedPreferences(context))
        }
    }

    fun getStringValue(key: String, defValue: String? = null): String? = sharedPreferences.getString(key, defValue)

    fun putStringValue(key: String, value: String?) = sharedPreferences.edit().putString(key, value).commit()

    fun removeStringValue(key: String) = sharedPreferences.edit().remove(key).commit()

    fun getBooleanValue(key: String, defValue: Boolean = false): Boolean = sharedPreferences.getBoolean(key, defValue)

    fun putBooleanValue(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).commit()

    fun getLongValue(key: String, defValue: Long = 0) = sharedPreferences.getLong(key, defValue)

    fun putLongValue(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).commit()

    fun removeLongValue(key: String) = sharedPreferences.edit().remove(key).commit()

    fun getIntValue(key: String, defValue: Int = 0) = sharedPreferences.getInt(key, defValue)

    fun putIntValue(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).commit()

    fun removeIntValue(key: String) = sharedPreferences.edit().remove(key).commit()
}