package com.example.manageincidents.domain.incidentUseCases

import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.domain.utils.UseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadIncidentsPhotosUseCase @Inject constructor(private val incidentRepository: IncidentRepository) :
    UseCase<UploadIncidentsPhotosUseCase.Request, ResultWrapper<Void>> {
    override suspend fun invoke(request: Request): ResultWrapper<Void> {
        return incidentRepository.uploadIncidentPhotos(request)
    }


    class Request(
        val incidentId: String,
        val image: MultipartBody.Part
    ): UseCase.Request



}
