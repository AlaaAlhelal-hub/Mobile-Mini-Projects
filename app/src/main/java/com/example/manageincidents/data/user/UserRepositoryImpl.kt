package com.example.manageincidents.data.user

import com.example.manageincidents.data.utils.safeApiCall
import com.example.manageincidents.domain.models.User
import com.example.manageincidents.domain.repository.UserRepository
import com.example.manageincidents.domain.userUseCases.GetAllUsersUseCase
import com.example.manageincidents.domain.userUseCases.LoginUseCase
import com.example.manageincidents.domain.userUseCases.OtpVerificationUseCase
import com.example.manageincidents.presentaion.base.ResultWrapper

class UserRepositoryImpl(private val retrofitService: UserRetrofitService) : UserRepository {
    override suspend fun login(request: LoginUseCase.Request): ResultWrapper<Void> {
        return safeApiCall { retrofitService.login(request) }
    }

    override suspend fun verifyOTP(request: OtpVerificationUseCase.Request): ResultWrapper<OtpVerificationUseCase.Response> {
        return safeApiCall { retrofitService.verifyOTP(request) }
    }

    override suspend fun getUsers(): ResultWrapper<List<User>> {
        return safeApiCall { retrofitService.getUsers() }
    }
}