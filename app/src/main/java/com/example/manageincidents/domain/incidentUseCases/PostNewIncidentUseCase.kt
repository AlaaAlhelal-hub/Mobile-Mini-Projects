package com.example.manageincidents.domain.incidentUseCases

import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.domain.utils.UseCase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostNewIncidentUseCase @Inject constructor(private val incidentRepository: IncidentRepository) :
    UseCase<PostNewIncidentUseCase.Request, ResultWrapper<PostNewIncidentUseCase.Response>> {
    override suspend fun invoke(request: Request): ResultWrapper<Response> {
        return incidentRepository.postNewIncident(request)
    }


    @JsonClass(generateAdapter = true)
    class Request(
        @Json(name = "description") val description: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "typeId") val typeId: Int
    ): UseCase.Request

    @JsonClass(generateAdapter = true)
    class Response(
        @Json(name = "id") val id: String
    )


}
