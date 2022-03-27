package com.example.manageincidents.presentaion.app.viewListOfIncident


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.domain.incidentUseCases.GetIncidentTypesUseCase
import com.example.manageincidents.domain.incidentUseCases.GetListOfIncidentUseCase
import com.example.manageincidents.domain.incidentUseCases.ViewIncidentTypesUseCase
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.domain.models.User
import com.example.manageincidents.domain.userUseCases.GetAllUsersUseCase
import com.example.manageincidents.domain.utils.ResponseError
import com.example.manageincidents.presentaion.base.ResultWrapper
import com.example.manageincidents.presentaion.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ListOfIncidentViewModel @Inject constructor(
    private val listOfIncidentUseCase: GetListOfIncidentUseCase,
    private val viewIncidentTypesUseCase: ViewIncidentTypesUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase)
    : ViewModel() {

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var _errorResponse = MutableLiveData<ApiErrorResponse>()
    val errorResponse : LiveData<ApiErrorResponse>
        get() = _errorResponse

    private var _listOfIncidents = MutableLiveData<List<Incident>>()
    val listOfIncidents : LiveData<List<Incident>>
        get() = _listOfIncidents


    private var _incidentType = MutableLiveData<List<IncidentType>>()
    val incidentType: LiveData<List<IncidentType>>
        get() = _incidentType

    private var _getTypeStatus = MutableLiveData<ApiStatus>()
    val getTypeStatus: LiveData<ApiStatus>
        get() = _getTypeStatus

    private var _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private var _issuerNames = MutableLiveData<ArrayList<String>>()
    val issuerNames: MutableLiveData<ArrayList<String>>
        get() = _issuerNames


    private var _nameListUpdated = MutableLiveData<Boolean>()
    val nameListUpdated: MutableLiveData<Boolean>
        get() = _nameListUpdated

    private var _getAllUsersStatus = MutableLiveData<ApiStatus>()
    val getAllUsersStatus: LiveData<ApiStatus>
        get() = _getAllUsersStatus

    private var _navigateToIncidentDetails = MutableLiveData<Incident?>()
    val navigateToIncidentDetails: MutableLiveData<Incident?>
        get() = _navigateToIncidentDetails

    var responseError = MutableLiveData<ResponseError>()

    fun onIncidentDetailsNavigated() {
        _navigateToIncidentDetails.value = null
    }

    fun onIncidentClicked(incident: Incident) {
        _navigateToIncidentDetails.value = incident
    }

    fun onNameListUpdatedFinished(){
        _nameListUpdated.value = false
    }

    init {
        getIncidentType()
        getListOfIncidents()
    }


    private fun getListOfIncidents() {
        val startDate = "2021-11-14"
        viewModelScope.launch {
            _status.value = ApiStatus.Pending
            _loadingStatus.value = true
            try {
                when (val response = listOfIncidentUseCase.invoke(GetListOfIncidentUseCase.Request(startDate))) {
                    is ResultWrapper.Success -> {
                        _status.value = ApiStatus.Done
                        _loadingStatus.value = false
                        _listOfIncidents.value = response.data.incidents
                        formattingDate()
                        getAllUsers()
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
                Log.i("getListOfIncidents", "$exception")
            }
        }
    }

    private fun getIncidentType() {

        viewModelScope.launch {
            _getTypeStatus.value = ApiStatus.Pending
            _loadingStatus.value = true
            try {
                when (val response = viewIncidentTypesUseCase.invoke(ViewIncidentTypesUseCase.Request())) {
                    is ResultWrapper.Success -> {
                        _getTypeStatus.value = ApiStatus.Done
                        _loadingStatus.value = false
                        _incidentType.value = response.data!!
                    }
                    is ResultWrapper.Error.ApiError -> {
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value = ResponseError(response.errorMessage, true)
                    }
                    is ResultWrapper.Error.TimeoutError -> {
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value = ResponseError("TimeOut Error", true)
                    }
                    is ResultWrapper.Error.ClientConnectionError -> {
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value =
                            ResponseError("Connection Error", true)
                    }
                    is ResultWrapper.Error.UnexpectedError ->{
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value =
                            ResponseError("Unexpected Error", true)
                    }
                }
            } catch (exception: Exception) {
                Log.i("getTypeOfIncident", "$exception")
            }
        }
    }

    private fun getAllUsers() {
        viewModelScope.launch {
            _getAllUsersStatus.value = ApiStatus.Pending
            _loadingStatus.value = true
            try {
                when (val response = getAllUsersUseCase.invoke(GetAllUsersUseCase.Request())) {
                    is ResultWrapper.Success -> {
                        _getAllUsersStatus.value = ApiStatus.Done
                        _loadingStatus.value = false
                        _users.value = response.data!!
                        getUserName()
                    }
                    is ResultWrapper.Error.ApiError -> {
                        _getAllUsersStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value = ResponseError(response.errorMessage, true)
                    }
                    is ResultWrapper.Error.TimeoutError -> {
                        _getAllUsersStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value = ResponseError("TimeOut Error", true)
                    }
                    is ResultWrapper.Error.ClientConnectionError -> {
                        _getAllUsersStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value =
                            ResponseError("Connection Error", true)
                    }
                    is ResultWrapper.Error.UnexpectedError ->{
                        _getAllUsersStatus.value = ApiStatus.Failure
                        _loadingStatus.value = true
                        responseError.value =
                            ResponseError("Unexpected Error", true)
                    }
                }
            } catch (exception: Exception) {
                Log.i("getAllUsersStatus", "$exception")
            }


        }
    }




    @SuppressLint("NewApi")
    private fun formattingDate(){
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        for (i in 0 until _listOfIncidents.value?.size!!){
            val dateCreatedAt = LocalDate.parse(_listOfIncidents.value!![i].createdAt , format)
            val dateUpdatedAt = LocalDate.parse(_listOfIncidents.value!![i].updatedAt , format)
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("en-US"))
            _listOfIncidents.value!![i].createdAt = dateCreatedAt.format(formatter)
            _listOfIncidents.value!![i].updatedAt = dateUpdatedAt.format(formatter)
        }

    }

    @SuppressLint("NewApi")
    private fun getUserName(){
        _issuerNames.value =  ArrayList<String>()
        for (i in 0 until _listOfIncidents.value?.size!!){
            loop@for (j in 0 until _users.value?.size!!){
                if( _listOfIncidents.value!![i].issuerId.equals(_users.value!![j].id)){
                    val email =_users.value!![j].email
                    _issuerNames.value!!.add(email!!.substring(email.indexOf('@')))
                    break@loop
                }
                else {
                    _issuerNames.value!!.add("not found")
                }
            }
        }
        _nameListUpdated.value = true
    }
    }


