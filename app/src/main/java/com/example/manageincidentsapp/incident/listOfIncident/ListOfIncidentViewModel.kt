package com.example.manageincidentsapp.incident.listOfIncident


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.manageincidentsapp.incident.Incident
import com.example.manageincidentsapp.incidentType.IncidentType
import com.example.manageincidentsapp.incidentType.SubIncidentType
import com.example.manageincidentsapp.network.ApiErrorResponse
import com.example.manageincidentsapp.network.IncidentApi
import com.example.manageincidentsapp.user.IncidentApiStatus
import com.example.manageincidentsapp.user.UserProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class ListOfIncidentViewModel(application: Application) : AndroidViewModel(application) {
    private val SHARED_PREFS = "shared_prefs"

    private val appContext = application
    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _status = MutableLiveData<IncidentApiStatus>()
    val status: LiveData<IncidentApiStatus>
        get() = _status


    private var _errorResponse = MutableLiveData<ApiErrorResponse>()
    val errorResponse : LiveData<ApiErrorResponse>
        get() = _errorResponse


    private var _listOfIncidents = MutableLiveData<List<Incident>?>()
    val listOfIncidents : MutableLiveData<List<Incident>?>
        get() = _listOfIncidents


    private var _incidentType = MutableLiveData<List<IncidentType>?>()
    val incidentType: MutableLiveData<List<IncidentType>?>
        get() = _incidentType

    private var _incidentSubtype = MutableLiveData<List<SubIncidentType>>()
    val incidentSubtype: LiveData<List<SubIncidentType>>
        get() = _incidentSubtype

    private var _getTypeStatus = MutableLiveData<IncidentApiStatus>()
    val getTypeStatus: LiveData<IncidentApiStatus>
        get() = _getTypeStatus

    private var _users = MutableLiveData<List<UserProperty>?>()
    val users: MutableLiveData<List<UserProperty>?>
        get() = _users

    private var _issuerNames = MutableLiveData<ArrayList<String>>()
    val issuerNames: MutableLiveData<ArrayList<String>>
        get() = _issuerNames


    private var _nameListUpdated = MutableLiveData<Boolean>()
    val nameListUpdated: MutableLiveData<Boolean>
        get() = _nameListUpdated

    private var _getAllUsersStatus = MutableLiveData<IncidentApiStatus>()
    val getAllUsersStatus: LiveData<IncidentApiStatus>
        get() = _getAllUsersStatus

    private var _navigateToIncidentDetails = MutableLiveData<Incident?>()
    val navigateToIncidentDetails: MutableLiveData<Incident?>
        get() = _navigateToIncidentDetails

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
        val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null).toString()

        val startDate = "2021-11-14"
        coroutineScope.launch {
            _status.value = IncidentApiStatus.Pending

            IncidentApi.retrofitService.getListOfIncident(token, startDate).enqueue( object: Callback<ListOfIncidentResponse> {
                override fun onFailure(call: Call<ListOfIncidentResponse>, t: Throwable) {
                    _status.value = IncidentApiStatus.Failure
                    _listOfIncidents.value = null
                    _errorResponse.value = ApiErrorResponse(null)
                    Log.i("Error ", "${t.message}")
                }

                override fun onResponse(call: Call<ListOfIncidentResponse>, response: Response<ListOfIncidentResponse>) {
                    Log.i("response code ", "${response.code()}")

                    if (response.code() == 200) {
                        _listOfIncidents.value = response.body()?.incidents?.asList()
                        formattingDate()
                        getAllUsers()
                        _status.value = IncidentApiStatus.Done
                    }
                    else if (response.code() == 403) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _listOfIncidents.value = null
                    }
                    else if (response.code() == 401) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _listOfIncidents.value = null
                    }
                }
            })

        }
    }

    private fun getIncidentType() {
        val sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("TOKEN", null).toString()


        coroutineScope.launch {
            _getTypeStatus.value = IncidentApiStatus.Pending

            IncidentApi.retrofitService.getIncidentType(token).enqueue( object:
                Callback<List<IncidentType>> {
                override fun onResponse(call: Call<List<IncidentType>>, response: Response<List<IncidentType>>
                ) {
                    Log.i("_incidentType response code ", "${response.code()}")

                    if (response.code() == 200) {
                        _incidentType.value = response.body()
                        _getTypeStatus.value = IncidentApiStatus.Done
                    }
                    else if (response.code() == 403) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _getTypeStatus.value = IncidentApiStatus.Failure
                        _incidentType.value = null
                    }
                    else if (response.code() == 401) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _getTypeStatus.value = IncidentApiStatus.Failure
                        _incidentType.value = null
                    }
                }

                override fun onFailure(call: Call<List<IncidentType>>, t: Throwable) {
                    _getTypeStatus.value = IncidentApiStatus.Failure
                    _incidentType.value = null
                    _errorResponse.value = ApiErrorResponse(null)
                    Log.i("_incidentType Error ", "${t.message}")
                }

            })
        }
    }

    private fun getAllUsers() {
        val sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null).toString()


        coroutineScope.launch {
            _getAllUsersStatus.value = IncidentApiStatus.Pending

            IncidentApi.retrofitService.getUsers(token).enqueue( object:
                Callback<List<UserProperty>> {
                override fun onResponse(call: Call<List<UserProperty>>, response: Response<List<UserProperty>>
                ) {
                    Log.i("_users response code ", "${response.code()}")

                    if (response.code() == 200) {
                        _users.value = response.body()
                        getUserName()
                        _getAllUsersStatus.value = IncidentApiStatus.Done
                    }
                    else if (response.code() == 403) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _getAllUsersStatus.value = IncidentApiStatus.Failure
                        _users.value = null
                    }
                    else if (response.code() == 401) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _getAllUsersStatus.value = IncidentApiStatus.Failure
                        _users.value = null
                    }
                }

                override fun onFailure(call: Call<List<UserProperty>>, t: Throwable) {
                    _getAllUsersStatus.value = IncidentApiStatus.Failure
                    _users.value = null
                    _errorResponse.value = ApiErrorResponse("${t.message}")
                    Log.i("user Error ", "${t.message}")
                }

            })
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}


