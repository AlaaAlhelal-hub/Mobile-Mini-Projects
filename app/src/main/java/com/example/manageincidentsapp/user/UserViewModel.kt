package com.example.manageincidentsapp.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.manageincidentsapp.network.ApiErrorResponse
import com.example.manageincidentsapp.network.IncidentApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


enum class IncidentApiStatus {Pending, Done, Failure}

class UserViewModel : ViewModel() {

    private var _loginStatus = MutableLiveData<IncidentApiStatus>()
    val loginStatus: LiveData<IncidentApiStatus>
        get() = _loginStatus


    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _otpStatus = MutableLiveData<IncidentApiStatus>()
    val otpStatus: LiveData<IncidentApiStatus>
        get() = _otpStatus

    private var _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse : MutableLiveData<LoginResponse?>
        get() = _loginResponse

    private var _user = MutableLiveData<UserProperty?>()
    val user : MutableLiveData<UserProperty?>
        get() = _user

    private var _errorHandler = MutableLiveData<ApiErrorResponse>()
    val errorHandler : LiveData<ApiErrorResponse>
        get() = _errorHandler

    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun checkUserEmail(email: String) {
        coroutineScope.launch {
            _loginStatus.value = IncidentApiStatus.Pending
            _loadingStatus.value = true
            val checkUser = UserProperty(email,"")
            IncidentApi.retrofitService.login(checkUser).enqueue( object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _user.value  = null
                    Log.i("response failed"," ${t.message}")
                    _loginStatus.value = IncidentApiStatus.Failure
                    _errorHandler.value = ApiErrorResponse(null)
                    _loadingStatus.value = false
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.i("response code"," ${response.code()}")

                    if (response.code() == 200) {
                        _user.value = UserProperty(email,"")
                        _loginStatus.value = IncidentApiStatus.Done
                        _loadingStatus.value = false
                    } else if (response.code() == 400){
                        _user.value  = null
                        _errorHandler.value = ApiErrorResponse("please enter a valid email")
                        _loginStatus.value = IncidentApiStatus.Failure
                        _loadingStatus.value = false
                    }
                }
            })
        }
    }


    fun otpVerification(user: UserProperty) {
        coroutineScope.launch {
            _otpStatus.value = IncidentApiStatus.Pending
            _loadingStatus.value = true

            IncidentApi.retrofitService.verifyOTP(user).enqueue( object: Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _otpStatus.value = IncidentApiStatus.Failure
                    _loginResponse.value = null
                    _errorHandler.value = ApiErrorResponse(null)
                    _loadingStatus.value = false
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.code() == 200) {
                        _loginResponse.value = LoginResponse(response.body()?.token!! , response.body()?.roles!!)
                        _otpStatus.value = IncidentApiStatus.Done
                        _loadingStatus.value = false

                    }
                    else {
                        _errorHandler.value = ApiErrorResponse("wrong code") //invalid credential
                        _otpStatus.value = IncidentApiStatus.Failure
                        _loginResponse.value = null
                        _loadingStatus.value = false
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

