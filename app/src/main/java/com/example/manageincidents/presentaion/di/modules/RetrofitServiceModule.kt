package com.example.manageincidents.presentaion.di.modules

import com.example.manageincidents.data.incident.IncidentRetrofitService
import com.example.manageincidents.data.user.UserRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)

@Module
class RetrofitServiceModule {

    @Provides
    @Singleton
    fun userRetrofitService(retrofit: Retrofit): UserRetrofitService {
        return retrofit.create(UserRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun incidentRetrofitService(retrofit: Retrofit): IncidentRetrofitService {
        return retrofit.create(IncidentRetrofitService::class.java)
    }
}