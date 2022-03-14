package com.example.manageincidentsapp.user

data class LoginResponse(
    var token: String,
    var roles: Array<Int>
)
