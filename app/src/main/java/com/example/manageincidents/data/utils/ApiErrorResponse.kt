package com.example.manageincidents.data.utils

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ApiErrorResponse(
    @Json(name = "errorMessage") var message: String
   // @Transient var httpCode: Int? = null
)