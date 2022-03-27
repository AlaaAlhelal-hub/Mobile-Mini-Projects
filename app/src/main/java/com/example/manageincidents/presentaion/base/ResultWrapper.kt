package com.example.manageincidents.presentaion.base

import java.lang.Exception


sealed class ResultWrapper<out T> {

    data class Success<T>(val data: T): ResultWrapper<T>()

    sealed class Error: ResultWrapper<Nothing>() {
        object UnAuthorizeError : Error()
        data class ApiError(val errorMessage: String, val httpCode: Int): Error()
        data class TimeoutError(val exception: Exception): Error()
        data class ClientConnectionError(val exception: Exception): Error()
        object ParsingError : Error()
        data class UnexpectedError(val exception: Exception): Error()
    }
}