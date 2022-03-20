package com.example.manageincidentsapp.network

import android.content.Context
import com.example.manageincidentsapp.incident.*
import com.example.manageincidentsapp.incident.addNewIncident.IncidentRequest
import com.example.manageincidentsapp.incident.addNewIncident.PostIncidentResponse
import com.example.manageincidentsapp.incident.dashboard.DashboardResponse
import com.example.manageincidentsapp.incident.editIncidentStatus.ChangeStatusRequest
import com.example.manageincidentsapp.incident.listOfIncident.ListOfIncidentResponse
import com.example.manageincidentsapp.incidentType.IncidentType
import com.example.manageincidentsapp.user.LoginResponse
import com.example.manageincidentsapp.user.UserProperty
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers


private const val BASE_URL = "https://mobilerapps.elm.sa/"
/*


var interceptor = HeadersInterceptor(SharedPreferenceManager.getInstance())


val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build()
*/
// Moshi object
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    //.client(client)
    .baseUrl(BASE_URL)
    .build()

object IncidentApi{
    val retrofitService : IncidentApiService by lazy {
        retrofit.create(IncidentApiService::class.java)
    }
}


interface IncidentApiService{


    @Headers("No-Authorization: true")
    @POST("login")
    fun login(@Body userEmail: UserProperty): Call<ResponseBody>

    @Headers("No-Authorization: true")
    @POST("verify-otp")
    fun verifyOTP(@Body user: UserProperty): Call<LoginResponse>

    @GET("incident")
    fun getListOfIncident(@Header("Authorization") token: String, @Query("startDate") startDate: String): Call<ListOfIncidentResponse>

    @GET("incident")
    fun addNewIncident(@Header("Authorization") token: String, @Query("description") description: String, @Query("typeId") typeId: Int, @Query("latitude") latitude: String, @Query("longitude") longitude: String): Call<ListOfIncidentResponse>

    @POST("incident")
    fun postNewIncident(@Header("Authorization") token: String, @Body incidentRequest: IncidentRequest): Call<PostIncidentResponse>

    @GET("types")
    fun getIncidentType(@Header("Authorization") token: String): Call<List<IncidentType>>

    @Multipart
    @POST("/incident/upload/{id}")
    fun uploadIncidentPhotos(@Header("Authorization") token: String, @Path("id") incidentId: String, @Part image: MultipartBody.Part): Call<Void>

    @GET("user")
    fun getUsers(@Header("Authorization") token: String): Call<List<UserProperty>>

    @PUT("incident/change-status")
    fun changeStatus(@Header("Authorization") token: String, @Body changeStatusRequest: ChangeStatusRequest): Call<Incident>

    @GET("dashboard")
    fun getDashboard(@Header("Authorization") token: String): Call<DashboardResponse>

}



