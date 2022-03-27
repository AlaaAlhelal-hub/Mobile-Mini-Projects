package com.example.manageincidents.data.user

import com.example.manageincidents.domain.models.User
import com.example.manageincidents.domain.userUseCases.LoginUseCase
import com.example.manageincidents.domain.userUseCases.OtpVerificationUseCase
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserRetrofitService {


    @POST("login")
    suspend fun login(@Body request: LoginUseCase.Request): Response<Void>

    @POST("verify-otp")
    suspend fun verifyOTP(@Body request: OtpVerificationUseCase.Request): Response<OtpVerificationUseCase.Response>

    @GET("user")
    suspend fun getUsers(): Response<List<User>>

}