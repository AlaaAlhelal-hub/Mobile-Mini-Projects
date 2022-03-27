/*
package com.example.manageincidents.presentaion.app.dashboard

import com.example.manageincidents.data.incident.IncidentRepositoryImpl
import com.example.manageincidents.data.incident.IncidentRetrofitService
import com.example.manageincidents.domain.incidentUseCases.GetDashboardUseCase
import com.example.manageincidents.domain.repository.IncidentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class DashboardModule {

    companion object {

       // @Provides
       // fun incidentRepository(service: IncidentRetrofitService): IncidentRepository = IncidentRepositoryImpl(service)


        @Provides
        fun getDashboardUseCase(repository: IncidentRepository) = GetDashboardUseCase(repository)


    }
}

 */