/*
package com.example.manageincidents.presentaion.app.addNewIncident

import com.example.manageincidents.presentaion.app.main.MainActivity

import com.example.manageincidents.data.incident.IncidentRepositoryImpl
import com.example.manageincidents.data.incident.IncidentRetrofitService
import com.example.manageincidents.domain.incidentUseCases.GetIncidentTypesUseCase
import com.example.manageincidents.domain.incidentUseCases.PostNewIncidentUseCase
import com.example.manageincidents.domain.incidentUseCases.UploadIncidentsPhotosUseCase
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.presentaion.utils.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AddNewIncidentModule {

    companion object {

       // @Provides
        //fun incidentRepository(service: IncidentRetrofitService): IncidentRepository = IncidentRepositoryImpl(service)

        @Provides
        fun postNewIncidentUseCase(repository: IncidentRepository) = PostNewIncidentUseCase(repository)

        @Provides
        fun getIncidentTypesUseCase(repository: IncidentRepository) = GetIncidentTypesUseCase(repository)

        @Provides
        fun uploadIncidentsPhotosUseCase(repository: IncidentRepository) = UploadIncidentsPhotosUseCase(repository)


    }
}

 */
