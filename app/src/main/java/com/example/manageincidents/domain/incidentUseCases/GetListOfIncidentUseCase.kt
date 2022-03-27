package com.example.manageincidents.domain.incidentUseCases


import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.domain.utils.UseCase
import com.squareup.moshi.JsonClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListOfIncidentUseCase @Inject constructor(private val repository: IncidentRepository):
    UseCase<GetListOfIncidentUseCase.Request, ResultWrapper<GetListOfIncidentUseCase.Response>> {

    override suspend fun invoke(request: Request): ResultWrapper<Response> {
        return repository.getListOfIncident(request)
    }

    class Request(val startDate: String): UseCase.Request

    @JsonClass(generateAdapter = true)
    class Response(
    val baseURL: String,
    val incidents: List<Incident>
    )

}
