package com.example.manageincidentsapp.user

data class UserProperty (
    var id: String?,
    var email: String?,
    var otp: String?,
    var token: String?,
    var createdAt: String?,
    var updatedAt: String?,
    var roles: List<UserRole>?
) {
    constructor(email: String, otp: String) : this(null, email, otp, null, null, null, null){}
    constructor(id: String) : this(id, null, null, null, null, null , null ){}
}

