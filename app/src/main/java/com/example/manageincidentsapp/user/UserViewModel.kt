package com.example.manageincidentsapp.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okhttp3.internal.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


enum class IncidentApiStatus {InProgress, Completed, Rejected}

class UserViewModel : ViewModel() {


    private var _loginStatus = MutableLiveData<IncidentApiStatus>()
    val loginStatus: LiveData<IncidentApiStatus>
        get() = _loginStatus

    private var _otpStatus = MutableLiveData<IncidentApiStatus>()
    val otpStatus: LiveData<IncidentApiStatus>
        get() = _otpStatus

    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse>
        get() = _loginResponse

    private var _user = MutableLiveData<UserProperty>()
    val user : LiveData<UserProperty>
        get() = _user

    private var _errorHandler = MutableLiveData<ErrorHandler>()
    val errorHandler : LiveData<ErrorHandler>
        get() = _errorHandler

    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun checkUserEmail(email: String) {
        coroutineScope.launch {
            _loginStatus.value = IncidentApiStatus.InProgress

            // val requestBody = JsonObject()
            // requestBody.addProperty("email", email)
            val checkUser = UserProperty(email, "")
            IncidentApi.retrofitService.login(checkUser).enqueue( object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _user.value  = null
                    Log.i("response failed"," ${t.message}")
                    _loginStatus.value = IncidentApiStatus.Rejected
                    _errorHandler.value = ErrorHandler(null)
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.i("response code"," ${response.code()}")

                    if (response.code() == 200) {
                        _user.value = UserProperty(email, "")
                        _loginStatus.value = IncidentApiStatus.Completed
                    } else if (response.code() == 400){
                        _user.value  = null
                        _errorHandler.value = ErrorHandler("please enter a valid email")
                        _loginStatus.value = IncidentApiStatus.Rejected
                    }
                }
            })
        }
    }


    fun otpVerification(user: UserProperty) {
        coroutineScope.launch {
            _otpStatus.value = IncidentApiStatus.InProgress

            IncidentApi.retrofitService.verifyOTP(user).enqueue( object: Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _otpStatus.value = IncidentApiStatus.Rejected
                    _loginResponse.value = null
                    _errorHandler.value = ErrorHandler(null)
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    if (response.code() == 200) {
                        _loginResponse.value = LoginResponse(response.body()?.token!! , response.body()?.roles!!)
                        _otpStatus.value = IncidentApiStatus.Completed
                    }
                    else {
                        _errorHandler.value = ErrorHandler("wrong code") //invalid credential
                        _otpStatus.value = IncidentApiStatus.Rejected
                        _loginResponse.value = null
                    }
                }
            })

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

