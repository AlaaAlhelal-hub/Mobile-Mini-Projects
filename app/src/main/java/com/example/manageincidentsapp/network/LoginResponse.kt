package com.example.manageincidentsapp.network

data class LoginResponse(
    var token: String,
    var roles: List<Int>
)
