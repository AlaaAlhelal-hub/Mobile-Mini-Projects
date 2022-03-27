package com.example.manageincidents.presentaion.app.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageincidents.R
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.domain.userUseCases.LoginUseCase
import com.example.manageincidents.domain.utils.ResponseError
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.presentaion.utils.PreferenceUtil
import com.example.manageincidents.presentaion.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val stringProvider: StringProvider,
    private val preferenceUtil: PreferenceUtil) : ViewModel() {

    private var _loginStatus = MutableLiveData<ApiStatus>()
    val loginStatus: LiveData<ApiStatus>
        get() = _loginStatus

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _errorHandler = MutableLiveData<ApiErrorResponse>()
    val errorHandler : LiveData<ApiErrorResponse>
        get() = _errorHandler

    var responseError = MutableLiveData<ResponseError>()

    var invalidEmail = MutableLiveData<InvalidEmail>()

    var emptyEmail = MutableLiveData<EmptyEmail>()


    fun onLoginClicked(email: String) {
        viewModelScope.launch {
            _loginStatus.value = ApiStatus.Pending
            _loadingStatus.value = true

            try {
                when (val response = loginUseCase.invoke(LoginUseCase.Request(email))) {
                    is ResultWrapper.Success -> {
                        _loginStatus.value = ApiStatus.Done
                        _loadingStatus.value = false
                        preferenceUtil.putStringValue(PreferenceUtil.USER_EMAIL, email)
                    }
                    is ResultWrapper.Error.ApiError -> {
                        _loginStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError(response.errorMessage, true)
                    }
                    is ResultWrapper.Error.TimeoutError -> {
                        _loginStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError("TimeOut Error", true)
                    }
                    is ResultWrapper.Error.ClientConnectionError -> {
                        _loginStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Connection Error", true)
                    }
                    is ResultWrapper.Error.UnexpectedError ->{
                        _loginStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Unexpected Error", true)
                        Log.i("THE exception ", "${response.exception}")
                    }


                }
            } catch (exception: LoginUseCase.InvalidLoginInputException) {
                _loginStatus.value = ApiStatus.Failure
                _loadingStatus.value = false
                for (error in exception.errors)
                    when (error) {
                        LoginUseCase.LoginInputValidator.ErrorType.EMPTY_Email -> emptyEmail.value =
                            EmptyEmail(stringProvider.provide(R.string.empty_email), true)
                        LoginUseCase.LoginInputValidator.ErrorType.INVALID_Email -> invalidEmail.value =
                            InvalidEmail(stringProvider.provide(R.string.invalid_email), true)
                    }
            }

        }
    }
    class EmptyEmail(var messageId: String? = null, var flag: Boolean = false)

    class InvalidEmail(var messageId: String? = null, var flag: Boolean = false)

}

