package com.example.manageincidents.domain.userUseCases

import com.example.manageincidents.domain.models.User
import com.example.manageincidents.domain.repository.UserRepository
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.domain.utils.UseCase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllUsersUseCase @Inject constructor(private val repository: UserRepository):
    UseCase<GetAllUsersUseCase.Request, ResultWrapper<List<User>>> {

    override suspend fun invoke(request: Request): ResultWrapper<List<User>> {
        return repository.getUsers()
    }

    class Request : UseCase.Request

/*
    @JsonClass(generateAdapter = true)
    class Response : ArrayList<ResponseItem>()

    data class ResponseItem(
        @Json(name = "id") val id: String,
        @Json(name = "email") val email: String,
        @Json(name = "otp") val otp: String,
        @Json(name = "roles") val roles: List<Role>,
        @Json(name = "token") val token: String,
        @Json(name = "createdAt") val createdAt: String,
        @Json(name = "updatedAt") val updatedAt: String
    )

    data class Role(
        @Json(name = "id") val id: String,
        @Json(name = "type") val type: Int,
        @Json(name = "userId") val userId: String
    )

 */
}
