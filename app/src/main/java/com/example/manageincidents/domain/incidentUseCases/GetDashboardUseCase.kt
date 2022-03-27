package com.example.manageincidents.domain.incidentUseCases

import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.domain.utils.UseCase
import com.example.manageincidents.presentaion.base.ResultWrapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDashboardUseCase @Inject constructor(private val repository: IncidentRepository):
UseCase<GetDashboardUseCase.Request, ResultWrapper<GetDashboardUseCase.Response>> {

    override suspend fun invoke(request: Request): ResultWrapper<Response> {
        return repository.getDashboard(request)
    }

    class Request() : UseCase.Request

    data class Response(
        val baseURL: String,
        val incidents: List<Incident>
    )

    data class Incident(
        val _count: Count,
        val status: Int
    )

    data class Count(
        val status: Int
    )

}
