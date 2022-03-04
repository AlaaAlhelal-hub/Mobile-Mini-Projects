package com.example.manageincidentsapp.user

data class LoginResponse(
    var token: String,
    var roles: Array<Int>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginResponse

        if (token != other.token) return false
        if (!roles.contentEquals(other.roles)) return false

        return true
    }

}
