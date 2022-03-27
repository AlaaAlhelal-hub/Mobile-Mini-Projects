package com.example.manageincidents.data.incident

import com.example.manageincidents.domain.incidentUseCases.ChangeStatusUseCase
import com.example.manageincidents.domain.incidentUseCases.GetDashboardUseCase
import com.example.manageincidents.domain.incidentUseCases.GetListOfIncidentUseCase
import com.example.manageincidents.domain.incidentUseCases.PostNewIncidentUseCase
import com.example.manageincidents.domain.models.IncidentType
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface IncidentRetrofitService {


    @GET("incident")
    suspend fun getListOfIncident(@Query("startDate") startDate: String): Response<GetListOfIncidentUseCase.Response>

    @POST("incident")
    suspend fun postNewIncident(@Body request: PostNewIncidentUseCase.Request): Response<PostNewIncidentUseCase.Response>

    @Multipart
    @POST("/incident/upload/{id}")
    suspend fun uploadIncidentPhotos(@Path("id") incidentId: String, @Part image: MultipartBody.Part): Response<Void>

    @PUT("incident/change-status")
    suspend fun changeStatus(@Body changeStatusRequest: ChangeStatusUseCase.Request): Response<ChangeStatusUseCase.Response>

    @GET("types")
    suspend fun getIncidentType(): Response<List<IncidentType>>

    @GET("dashboard")
    suspend fun getDashboard(): Response<GetDashboardUseCase.Response>

}