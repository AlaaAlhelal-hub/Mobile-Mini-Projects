package com.example.manageincidentsapp.user

import com.example.manageincidentsapp.incident.Incident
import com.example.manageincidentsapp.incident.ListOfIncidentResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
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

    /**
    @FormUrlEncoded
    @POST("login")
    fun login(@FieldMap email: Map<String?, String?>?): Call<UserProperty?>?
    **/


    @POST("login")
    fun login(@Body userEmail: UserProperty): Call<ResponseBody>

    @POST("verify-otp")
    fun verifyOTP(@Body user: UserProperty): Call<LoginResponse>

    @GET("incident")
    fun getListOfIncident(@Header("Authorization") token: String, @Query("startDate") startDate: String): Call<ListOfIncidentResponse>
}



