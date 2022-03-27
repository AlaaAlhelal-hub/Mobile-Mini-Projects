/*
package com.example.manageincidents.presentaion.app.viewListOfIncident

import com.example.manageincidents.data.incident.IncidentRepositoryImpl
import com.example.manageincidents.data.incident.IncidentRetrofitService
import com.example.manageincidents.data.user.UserRepositoryImpl
import com.example.manageincidents.data.user.UserRetrofitService
import com.example.manageincidents.domain.incidentUseCases.GetIncidentTypesUseCase
import com.example.manageincidents.domain.incidentUseCases.GetListOfIncidentUseCase
import com.example.manageincidents.domain.incidentUseCases.ViewIncidentTypesUseCase
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.domain.repository.UserRepository
import com.example.manageincidents.domain.userUseCases.GetAllUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent



@Module
@InstallIn(ViewModelComponent::class)
abstract class ListOfIncidentModule {

        companion object {
              //  @Provides
             //   fun incidentRepository(service: IncidentRetrofitService): IncidentRepository = IncidentRepositoryImpl(service)

               // @Provides
                //fun userRepository(service: UserRetrofitService): UserRepository = UserRepositoryImpl(service)

                @Provides
                fun viewIncidentTypesUseCase(repository: IncidentRepository) =
                        ViewIncidentTypesUseCase(repository)

                @Provides
                fun getListOfIncidentUseCase(repository: IncidentRepository) =
                        GetListOfIncidentUseCase(repository)

                @Provides
                fun getAllUsersUseCase(repository: UserRepository) = GetAllUsersUseCase(repository)
        }

}

 */
