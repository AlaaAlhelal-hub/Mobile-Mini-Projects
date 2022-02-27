package com.example.manageincidentsapp.user

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://mobilerapps.elm.sa/"

// Moshi object
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

object IncidentApi{
    val retrofitService : IncidentApiService by lazy {
        retrofit.create(IncidentApiService::class.java)
    }
}

interface IncidentApiService {

    @POST("login")
    fun login(@Body email: String): Call<UserProperty>

    @FormUrlEncoded
    @POST("verify-otp")
    fun verifyOTP(@Field("email") email: String, @Field("otp") otp: String): Call<LoginResponse>
}



