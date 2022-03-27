package com.example.manageincidents.data.incident

import com.example.manageincidents.data.utils.safeApiCall
import com.example.manageincidents.domain.incidentUseCases.*
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.presentaion.base.ResultWrapper


class IncidentRepositoryImpl(private val retrofitService: IncidentRetrofitService) : IncidentRepository {

    override suspend fun getListOfIncident(request: GetListOfIncidentUseCase.Request): ResultWrapper<GetListOfIncidentUseCase.Response> {
        return safeApiCall { retrofitService.getListOfIncident(request.startDate) }
    }

    override suspend fun postNewIncident(request: PostNewIncidentUseCase.Request): ResultWrapper<PostNewIncidentUseCase.Response> {
        return safeApiCall { retrofitService.postNewIncident(request) }
    }

    override suspend fun changeStatus(request: ChangeStatusUseCase.Request): ResultWrapper<Incident> {
        return safeApiCall { retrofitService.changeStatus(request) }
    }

    override suspend fun uploadIncidentPhotos(request: UploadIncidentsPhotosUseCase.Request
    ): ResultWrapper<Void> {
        return safeApiCall { retrofitService.uploadIncidentPhotos(request.incidentId, request.image) }
    }

    override suspend fun getIncidentTypes(request: GetIncidentTypesUseCase.Request): ResultWrapper<List<IncidentType>> {
        return safeApiCall { retrofitService.getIncidentType() }
    }

    override suspend fun getDashboard(request: GetDashboardUseCase.Request): ResultWrapper<GetDashboardUseCase.Response> {
        return safeApiCall { retrofitService.getDashboard() }
    }

    override suspend fun viewIncidentTypes(request: ViewIncidentTypesUseCase.Request): ResultWrapper<List<IncidentType>> {
        return safeApiCall { retrofitService.getIncidentType() }
    }


}