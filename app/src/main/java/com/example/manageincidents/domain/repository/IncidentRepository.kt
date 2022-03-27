package com.example.manageincidents.domain.repository

import com.example.manageincidents.domain.incidentUseCases.*
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.presentaion.base.ResultWrapper

interface IncidentRepository {
    suspend fun getListOfIncident(request: GetListOfIncidentUseCase.Request): ResultWrapper<GetListOfIncidentUseCase.Response>
    suspend fun postNewIncident(request: PostNewIncidentUseCase.Request): ResultWrapper<PostNewIncidentUseCase.Response>
    suspend fun changeStatus(request: ChangeStatusUseCase.Request): ResultWrapper<Incident>
    suspend fun uploadIncidentPhotos(request: UploadIncidentsPhotosUseCase.Request): ResultWrapper<Void>
    suspend fun getIncidentTypes(request: GetIncidentTypesUseCase.Request): ResultWrapper<List<IncidentType>>
    suspend fun getDashboard(request: GetDashboardUseCase.Request):ResultWrapper<GetDashboardUseCase.Response>
    suspend fun viewIncidentTypes(request: ViewIncidentTypesUseCase.Request): ResultWrapper<List<IncidentType>>

}