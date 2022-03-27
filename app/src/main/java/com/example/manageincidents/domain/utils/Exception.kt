package com.example.manageincidents.domain.utils


open class AppException(private val errorMessage: String? = null, private val exception: Throwable? = null)
    : Exception(errorMessage, exception)



