package com.example.manageincidents.presentaion.di.modules

import android.content.Context
import com.example.manageincidents.data.incident.IncidentRepositoryImpl
import com.example.manageincidents.data.incident.IncidentRetrofitService
import com.example.manageincidents.data.user.UserRepositoryImpl
import com.example.manageincidents.data.user.UserRetrofitService
import com.example.manageincidents.domain.repository.IncidentRepository
import com.example.manageincidents.domain.repository.UserRepository
import com.example.manageincidents.presentaion.utils.LocaleUtil
import com.example.manageincidents.presentaion.utils.PreferenceUtil
import com.example.manageincidents.presentaion.utils.Session
import com.example.manageincidents.presentaion.utils.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext context: Context) = StringProvider(context)


    @Provides
    @Singleton
    fun providePreferenceUtil(@ApplicationContext context: Context) = PreferenceUtil(context)

    @Provides
    @Singleton
    fun provideLocaleUtil(@ApplicationContext app:Context) = LocaleUtil(app)

    @Provides
    @Singleton
    fun provideSession(preferenceUtil: PreferenceUtil, localeUtil: LocaleUtil) = Session(preferenceUtil, localeUtil)

    @Provides
    fun incidentRepository(service: IncidentRetrofitService): IncidentRepository = IncidentRepositoryImpl(service)

    @Provides
    fun userRepository(service: UserRetrofitService): UserRepository = UserRepositoryImpl(service)

    //@Provides
    //fun cookieDelegate (): CookieDelegate = CookieDelegate()


}