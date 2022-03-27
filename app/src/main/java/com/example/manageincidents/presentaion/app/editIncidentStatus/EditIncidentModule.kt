/*
package com.example.manageincidents.presentaion.app.editIncidentStatus


import com.example.manageincidents.data.incident.IncidentRepositoryImpl
import com.example.manageincidents.data.incident.IncidentRetrofitService
import com.example.manageincidents.domain.incidentUseCases.ChangeStatusUseCase
import com.example.manageincidents.domain.incidentUseCases.GetDashboardUseCase
import com.example.manageincidents.domain.repository.IncidentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class EditIncidentModule {

    companion object {

       // @Provides
        //fun incidentRepository(service: IncidentRetrofitService): IncidentRepository = IncidentRepositoryImpl(service)


        @Provides
        fun changeStatusUseCase(repository: IncidentRepository) = ChangeStatusUseCase(repository)


    }
}
*/
