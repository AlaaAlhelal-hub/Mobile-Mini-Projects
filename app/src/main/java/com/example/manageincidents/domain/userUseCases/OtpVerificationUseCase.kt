package com.example.manageincidents.domain.userUseCases

import com.example.manageincidents.domain.repository.UserRepository
import com.example.manageincidents.domain.utils.AppException
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.domain.utils.UseCase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtpVerificationUseCase @Inject constructor(private val repository: UserRepository):
    UseCase<OtpVerificationUseCase.Request, ResultWrapper<OtpVerificationUseCase.Response>> {

    override suspend fun invoke(request: Request): ResultWrapper<Response> {
        val validation = otpInputValidator()
        if (!validation.isValid(request.otp)){
            throw InvalidOTPInputException(validation.errors)
        }
        return repository.verifyOTP(request)
    }

    @JsonClass(generateAdapter = true)
    class Request(@Json(name = "email") val email: String,
                  @Json(name = "otp") val otp: String) : UseCase.Request

    @JsonClass(generateAdapter = true)
    class Response(
        @Json(name = "token") val token: String,
        @Json(name = "roles") val roles: Array<Int>
    )

    class otpInputValidator {
        enum class ErrorType {
            EMPTY_OTP
        }
        lateinit var errors :MutableList<ErrorType>

        fun isValid(otp: String): Boolean {
            errors = mutableListOf()
            if (otp.isEmpty()) {
                errors.add(ErrorType.EMPTY_OTP)
                return false
            }
            return true
        }
    }

    class InvalidOTPInputException(val errors: List<otpInputValidator.ErrorType>) :
        AppException()


}