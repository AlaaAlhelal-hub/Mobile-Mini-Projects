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
class LoginUseCase @Inject constructor(private val repository: UserRepository):
    UseCase<LoginUseCase.Request, ResultWrapper<Void>> {

    override suspend fun invoke(request: Request): ResultWrapper<Void> {
        val validation = LoginInputValidator()
        if (!validation.isValid(request.email)){
            throw InvalidLoginInputException(validation.errors)
        }
        return repository.login(request)
    }


    @JsonClass(generateAdapter = true)
    class Request(@Json(name = "email") val email: String) : UseCase.Request



    class LoginInputValidator {
        enum class ErrorType {
            INVALID_Email,
            EMPTY_Email
        }

        lateinit var errors :MutableList<ErrorType>

        fun isValid(email: String): Boolean {
            errors = mutableListOf()

            if (email.isEmpty()) {
                errors.add(ErrorType.EMPTY_Email)

                return false
                //android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            } else if (false) {
                errors.add(ErrorType.INVALID_Email)

                return false
            }

            return true
        }
    }

    class InvalidLoginInputException(val errors: List<LoginInputValidator.ErrorType>) :
        AppException()

}