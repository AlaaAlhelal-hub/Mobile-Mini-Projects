package com.example.manageincidents.presentaion.app.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.domain.incidentUseCases.GetDashboardUseCase
import com.example.manageincidents.domain.utils.ResponseError
import com.example.manageincidents.presentaion.base.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardUseCase: GetDashboardUseCase) : ViewModel() {


    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _errorResponse = MutableLiveData<ApiErrorResponse>()
    val errorResponse : LiveData<ApiErrorResponse>
        get() = _errorResponse

    var responseError = MutableLiveData<ResponseError>()

    private var _dashboardData = MutableLiveData<List<GetDashboardUseCase.Incident>>()
    val dashboardData : MutableLiveData<List<GetDashboardUseCase.Incident>>
        get() = _dashboardData

    init {
        getDashboard()
    }


    fun getDashboard(){

        viewModelScope.launch {
            _status.value = ApiStatus.Pending
            _loadingStatus.value = true
            try {
                when (val response = getDashboardUseCase.invoke(GetDashboardUseCase.Request())) {
                    is ResultWrapper.Success -> {
                        _loadingStatus.value = false
                        _dashboardData.value = response.data.incidents
                        _status.value = ApiStatus.Done

                    }
                    is ResultWrapper.Error.ApiError -> {
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value = ResponseError(response.errorMessage, true)
                    }
                    is ResultWrapper.Error.TimeoutError -> {
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value = ResponseError("TimeOut Error", true)
                    }
                    is ResultWrapper.Error.ClientConnectionError -> {
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value =
                            ResponseError("Connection Error", true)
                    }
                    is ResultWrapper.Error.UnexpectedError ->{
                        _status.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value =
                            ResponseError("Unexpected Error", true)
                    }
                }
            } catch (exception: Exception) {
                Log.i("get dashboard ", "$exception")
            }
        }
    }


}