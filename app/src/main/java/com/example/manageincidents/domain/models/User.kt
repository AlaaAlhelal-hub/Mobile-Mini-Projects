package com.example.manageincidents.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.*

@JsonClass(generateAdapter = true)
class User(
    @Json(name = "id") var id: String?,
    @Json(name = "email") var email: String?,
    @Json(name = "otp") var otp: String?,
    @Json(name = "token") var token: String?,
    @Json(name = "createdAt") var createdAt: String?,
    @Json(name = "updatedAt") var updatedAt: String?,
    @Json(name = "roles") var roles: List<UserRole>?,

) : Serializable {
    constructor(email: String, token: String) : this(null, email, null, token, null, null, null){}
    constructor(id: String) : this(id, null, null, null, null, null , null){}
}

@JsonClass(generateAdapter = true)
class UserRole(
    @Json(name = "id") var id: String,
    @Json(name = "type") var type: Int,
    @Json(name = "userId") var userId: String
) : Serializable
