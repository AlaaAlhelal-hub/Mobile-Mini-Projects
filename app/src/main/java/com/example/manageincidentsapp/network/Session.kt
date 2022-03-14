package com.example.manageincidentsapp.network
import com.example.manageincidentsapp.network.SharedPreferenceManager.Companion.KEY_TOKEN

class Session constructor(val sharedPreferenceManager: SharedPreferenceManager) {
    override fun toString(): String {

              return  "mToken:$mToken "
    }

    var mToken: String? = null
        get() {
            if (field == null)
                field = sharedPreferenceManager.getStringValue(KEY_TOKEN)
            return field
        }
        set(value) {
            field = value
            if (value == null)
                sharedPreferenceManager.removeStringValue(KEY_TOKEN)
            else
                sharedPreferenceManager.putStringValue(KEY_TOKEN, value)
        }
}