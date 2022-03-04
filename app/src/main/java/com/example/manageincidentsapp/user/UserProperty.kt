package com.example.manageincidentsapp.user

import com.google.gson.annotations.SerializedName

data class UserProperty (
    @SerializedName("email")
    var email: String,
    @SerializedName("otp")
    var otp: String
)

