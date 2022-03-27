package com.example.manageincidents.presentaion.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.manageincidents.domain.models.User
import com.google.gson.Gson
import java.lang.reflect.Type

class PreferenceUtil(private val mContext: Context) {

    companion object {
        const val USER_EMAIL= "USER_EMAIL"
        const val TOKEN = "TOKEN"
        const val USER_DATA_KEY = "USER_DATA_KEY"
        const val KEY_LANGUAGE = "keyLanguage"
    }

    private val defaultEditor: SharedPreferences.Editor
        get() = defaultSharedPreferences.edit()

    private val defaultSharedPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(mContext)

    fun putStringValue(key: String, value: String?) {
        defaultSharedPreferences.edit()?.putString(key, value)?.apply()
    }

    fun putUserDetails(userDetails: User?) {
        var json: String? = null
        if(userDetails != null){
            json = Gson().toJson(userDetails)
        }
        json?.let { putStringValue(USER_DATA_KEY, it) }
    }

    fun putIntValue(key: String, value: Int) {
        defaultSharedPreferences.edit()?.putInt(key, value)?.apply()
    }

    fun putBooleanValue(key: String, value: Boolean) {
        defaultSharedPreferences.edit()?.putBoolean(key, value)?.apply()
    }


    fun getStringValue(key: String): String {
        val sharedPreferences = defaultSharedPreferences
        return sharedPreferences.getString(key, "")!!
    }

    fun getStringValue(key: String, defaultValue: String?): String? {
        val sharedPreferences = defaultSharedPreferences
        return sharedPreferences.getString(key, defaultValue)
    }

    fun getUserDetails(): User? {
        val userDetails = getStringValue(USER_DATA_KEY)
        return Gson().fromJson(userDetails, User::class.java)
    }


    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean? {
        val sharedPreferences = defaultSharedPreferences
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        val sharedPreferences = defaultSharedPreferences
        return sharedPreferences.getInt(key, defaultValue)
    }

    operator fun <T> get(key: String, clazz: Class<T>): Any? {
        val value = getStringValue(key)
        return null
    }


    fun clear() {
        defaultEditor.clear().commit()
    }

    fun clear(userKey: String) {
        defaultEditor.remove(userKey).commit()
    }
}
