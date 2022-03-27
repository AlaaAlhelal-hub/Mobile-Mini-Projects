package com.example.manageincidents.domain.incidentUseCases

import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.domain.utils.UseCase
import com.example.manageincidents.presentaion.base.ResultWrapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetIncidentTypesUseCase @Inject constructor(private val repository: IncidentRepository):
UseCase<GetIncidentTypesUseCase.Request, ResultWrapper<List<IncidentType>>> {

    override suspend fun invoke(request: Request): ResultWrapper<List<IncidentType>> {
        return repository.getIncidentTypes(request)
    }

    class Request() : UseCase.Request

}

