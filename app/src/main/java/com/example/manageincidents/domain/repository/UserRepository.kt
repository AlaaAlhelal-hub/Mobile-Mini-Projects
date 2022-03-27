package com.example.manageincidents.domain.repository

import com.example.manageincidents.domain.models.User
import com.example.manageincidents.domain.userUseCases.LoginUseCase
import com.example.manageincidents.domain.userUseCases.OtpVerificationUseCase
import com.example.manageincidents.presentaion.base.ResultWrapper


interface UserRepository {
    suspend fun login(request: LoginUseCase.Request): ResultWrapper<Void>
    suspend fun verifyOTP(request: OtpVerificationUseCase.Request): ResultWrapper<OtpVerificationUseCase.Response>
    suspend fun getUsers(): ResultWrapper<List<User>>
}