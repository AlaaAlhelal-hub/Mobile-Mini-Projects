package com.example.manageincidentsapp.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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


    private var _property = MutableLiveData<LoginResponse>()
    val property : LiveData<LoginResponse>
        get() = _property

    private var _user = MutableLiveData<UserProperty>()
    val user : LiveData<UserProperty>
        get() = _user

    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun getUserProperties(email: String) {
        coroutineScope.launch {
            _loginStatus.value = IncidentApiStatus.InProgress

            IncidentApi.retrofitService.login(email).enqueue( object: Callback<UserProperty> {
                override fun onFailure(call: Call<UserProperty>, t: Throwable) {
                    _loginStatus.value = IncidentApiStatus.Rejected
                    _user.value?.email = ""
                }
                override fun onResponse(call: Call<UserProperty>, response: Response<UserProperty>) {
                    _user.value?.email = email
                    _loginStatus.value = IncidentApiStatus.Completed
                }
            })
        }
    }


    fun otpVerification(email: String, otp: String) {
        coroutineScope.launch {
            _otpStatus.value = IncidentApiStatus.InProgress

            IncidentApi.retrofitService.verifyOTP(email, otp).enqueue( object: Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _otpStatus.value = IncidentApiStatus.Rejected
                    _property.value = null
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.body()?.roles != null && response.body()?.token != null) {
                        _property.value?.roles = response.body()?.roles!!
                        _property.value?.token = response.body()?.token!!
                        _otpStatus.value = IncidentApiStatus.Completed
                    }
                    else {
                        _otpStatus.value = IncidentApiStatus.Rejected
                        _property.value = null
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

