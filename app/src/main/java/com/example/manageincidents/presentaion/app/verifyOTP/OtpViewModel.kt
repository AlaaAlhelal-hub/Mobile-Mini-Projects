package com.example.manageincidents.presentaion.app.verifyOTP

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageincidents.R
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.domain.models.User
import com.example.manageincidents.domain.userUseCases.OtpVerificationUseCase
import com.example.manageincidents.domain.utils.ResponseError
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.presentaion.utils.PreferenceUtil
import com.example.manageincidents.presentaion.utils.Session
import com.example.manageincidents.presentaion.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val otpUseCase: OtpVerificationUseCase,
    private val stringProvider: StringProvider,
    private val preferenceUtil: PreferenceUtil,
    private val session: Session
) : ViewModel(){

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _loginResponse = MutableLiveData<OtpVerificationUseCase.Response>()
    val loginResponse : LiveData<OtpVerificationUseCase.Response>
        get() = _loginResponse


    private var _errorHandler = MutableLiveData<ApiErrorResponse>()
    val errorHandler : LiveData<ApiErrorResponse>
        get() = _errorHandler


    var responseError = MutableLiveData<ResponseError>()

    var emptyOtp = MutableLiveData<EmptyOTP>()



    fun onDoneClicked(otp: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.Pending
            _loadingStatus.value = true

            try {
                val email = preferenceUtil.getStringValue(PreferenceUtil.USER_EMAIL)
                when (val response = otpUseCase.invoke(OtpVerificationUseCase.Request(email, otp))) {
                    is ResultWrapper.Success -> {
                        _status.value = ApiStatus.Done
                        _loadingStatus.value = false
                        val token = response.data.token
                        _loginResponse.value = response.data!!
                        session.accessToken = token
                       // preferenceUtil.putStringValue(PreferenceUtil.TOKEN, token)
                        //add roles
                        val userData = User(email = email, token = token)
                        session.user = userData
                        Timber.i("Ok")
                    }
                    is ResultWrapper.Error.ApiError -> {
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError(response.errorMessage, true)
                        Log.i("Response ","${response.errorMessage}")

                    }
                    is ResultWrapper.Error.TimeoutError -> {
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError("TimeOut Error", true)
                        Log.i("Response ","${response.exception}")

                    }
                    is ResultWrapper.Error.ClientConnectionError -> {
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Connection Error", true)
                        Log.i("Response ","${response.exception}")

                    }
                    is ResultWrapper.Error.UnexpectedError ->{
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Unexpected Error", true)
                        Log.i("Response ","${response.exception}")

                    }
                }
            } catch (exception: OtpVerificationUseCase.InvalidOTPInputException) {
                _status.value = ApiStatus.Failure
                _loadingStatus.value = false
                for (error in exception.errors)
                    when (error) {
                        OtpVerificationUseCase.otpInputValidator.ErrorType.EMPTY_OTP -> emptyOtp.value =
                            EmptyOTP(
                                stringProvider.provide(R.string.empty_otp),
                                true
                            )
                    }
                Log.i("Response ","Empty otp")

            }
        }
    }



    class EmptyOTP(var messageId: String? = null, var flag: Boolean = false)


}

