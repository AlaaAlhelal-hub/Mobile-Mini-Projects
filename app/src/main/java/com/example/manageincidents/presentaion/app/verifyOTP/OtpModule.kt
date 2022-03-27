/*
package com.example.manageincidents.presentaion.app.verifyOTP

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidents.data.user.UserRepositoryImpl
import com.example.manageincidents.data.user.UserRetrofitService
import com.example.manageincidents.domain.repository.UserRepository
import com.example.manageincidents.domain.userUseCases.OtpVerificationUseCase
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
abstract class OtpModule {

    companion object {
        //@Provides
        //fun userRepository(service: UserRetrofitService): UserRepository = UserRepositoryImpl(service)

        @Provides
        fun otpVerificationUseCase(repository: UserRepository) = OtpVerificationUseCase(repository)
/*

        @Provides
        fun provideStringProvider(otpActivity: OtpVerificationActivity): StringProvider {
            return StringProvider(otpActivity)
        }

 */
    }
/*
    @Binds
    @IntoMap
    @ViewModelKey(OtpViewModel::class)
    abstract fun otpViewModel(otpViewModel: OtpViewModel): ViewModel
*/
}

*/