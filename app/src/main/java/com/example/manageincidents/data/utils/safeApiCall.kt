package com.example.manageincidents.data.utils

import com.example.manageincidents.presentaion.base.ResultWrapper
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException


suspend fun <Q, T> safeApiCall(call: suspend () -> Response<T>): ResultWrapper<Q> {
    return withContext(Dispatchers.IO) {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                if(response.body()!=null) {
                    val body = response.body()!!
                    ResultWrapper.Success(body) as ResultWrapper<Q>
                }
                else
                {
                    ResultWrapper.Success(null) as ResultWrapper<Q>
                }
            } else {
                val httpCode = response.code()
                if (httpCode == 401) ResultWrapper.Error.UnAuthorizeError

                val errorBody = response.errorBody()
                val apiErrorResponse = convertErrorBody(errorBody)

                if (apiErrorResponse != null) {
                    ResultWrapper.Error.ApiError(apiErrorResponse.message, httpCode)
                } else {
                    if(httpCode == 401){
                        ResultWrapper.Error.UnAuthorizeError
                    }else {
                        ResultWrapper.Error.ParsingError
                    }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is ConnectException, is UnknownHostException, is SSLException -> ResultWrapper.Error.ClientConnectionError(e)
                is SocketTimeoutException -> ResultWrapper.Error.TimeoutError(e)
                else -> ResultWrapper.Error.UnexpectedError(e)
            }
        }
    }
}

private fun convertErrorBody(errorBody: ResponseBody?): ApiErrorResponse? {
    return try {
        errorBody?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ApiErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        exception.printStackTrace()
        null
    }
}