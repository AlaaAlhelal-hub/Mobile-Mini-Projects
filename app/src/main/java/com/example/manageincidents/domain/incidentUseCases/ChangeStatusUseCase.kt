package com.example.manageincidents.domain.incidentUseCases

import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.domain.utils.UseCase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangeStatusUseCase @Inject constructor(private val incidentRepository: IncidentRepository) :
    UseCase<ChangeStatusUseCase.Request, ResultWrapper<Incident>> {
    override suspend fun invoke(request: Request): ResultWrapper<Incident> {
        return incidentRepository.changeStatus(request)
    }


    @JsonClass(generateAdapter = true)
    class Request(
        @Json(name = "incidentId") val incidentId: String,
        @Json(name = "status")  val status: Int
    ): UseCase.Request

    @JsonClass(generateAdapter = true)
    class Response(
        @Json(name = "id") val id: String,
        @Json(name = "assigneeId") val assigneeId: Any,
        @Json(name = "createdAt") val createdAt: String,
        @Json(name = "description") val description: String,
        @Json(name = "issuerId") val issuerId: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "priority") val priority: Any,
        @Json(name = "status") val status: Int,
        @Json(name = "typeId") val typeId: Int,
        @Json(name = "updatedAt") val updatedAt: String
        )

}
