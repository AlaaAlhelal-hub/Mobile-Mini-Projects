/*
package com.example.manageincidents.presentaion.app.main

import androidx.lifecycle.ViewModel
import com.example.manageincidents.data.user.UserRepositoryImpl
import com.example.manageincidents.data.user.UserRetrofitService
import com.example.manageincidents.domain.repository.UserRepository
import com.example.manageincidents.domain.userUseCases.LoginUseCase
import com.example.manageincidents.presentaion.di.qualifiers.ActivityScope
import com.example.manageincidents.presentaion.di.qualifiers.ViewModelKey
import com.example.manageincidents.presentaion.utils.StringProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginModule {

    companion object {

       // @Provides
      //  fun userRepository(service: UserRetrofitService): UserRepository = UserRepositoryImpl(service)

        @Provides
        fun loginUseCase(repository: UserRepository) = LoginUseCase(repository)

      /*  @Provides
        fun provideStringProvider(loginActivity: MainActivity): StringProvider {
            return StringProvider(loginActivity)
        }

       */
    }

    /*
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel
*/
}

 */