package com.example.manageincidents.presentaion.app.editIncidentStatus

import android.util.Log
import androidx.lifecycle.*
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.domain.incidentUseCases.ChangeStatusUseCase
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.domain.utils.ResponseError
import com.example.manageincidents.presentaion.base.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditIncidentStatusViewModel @Inject constructor(/*
     val incidentId: String,
     val currentStatus: Int,
     val incidentType: IncidentType,
     val issuerName: String,
     */
    private val changeStatusUseCase: ChangeStatusUseCase) : ViewModel() {



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

    private var _incidentIdValue = MutableLiveData<String>()
    val incidentIdValue : MutableLiveData<String>
        get() = _incidentIdValue

    private var _incident = MutableLiveData<Incident>()
    val incident : MutableLiveData<Incident>
        get() = _incident

    private var _incidentTypeValue = MutableLiveData<IncidentType?>()
    val incidentTypeValue : MutableLiveData<IncidentType?>
        get() = _incidentTypeValue

    private var _issuerNameValue = MutableLiveData<String?>()
    val issuerNameValue : MutableLiveData<String?>
        get() = _issuerNameValue

    private var _currentStatusValue = MutableLiveData<Int>()
    val currentStatusValue : MutableLiveData<Int>
        get() = _currentStatusValue
/*
    init {
        _issuerNameValue.value = issuerName
        _incidentTypeValue.value = incidentType
        _incidentIdValue.value = incidentId
        _currentStatusValue.value = currentStatus
    }
*/

    fun changeStatus(newStatus: Int) {

        viewModelScope.launch {
            _status.value = ApiStatus.Pending
            _loadingStatus.value = true
            try {
                when (val response = changeStatusUseCase.invoke(ChangeStatusUseCase.Request(_incidentIdValue.value!!, newStatus))) {
                    is ResultWrapper.Success -> {
                        _status.value = ApiStatus.Done
                        _loadingStatus.value = false
                        _incident.value = response.data!!
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
                Log.i("change status", "$exception")
            }
        }
    }


}
