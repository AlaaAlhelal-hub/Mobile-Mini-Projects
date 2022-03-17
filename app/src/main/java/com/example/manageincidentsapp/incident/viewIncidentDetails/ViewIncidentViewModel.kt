package com.example.manageincidentsapp.incident.viewIncidentDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.manageincidentsapp.incident.Incident
import com.example.manageincidentsapp.incidentType.IncidentType
import com.example.manageincidentsapp.network.ApiErrorResponse
import com.example.manageincidentsapp.user.IncidentApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class ViewIncidentViewModel(incident: Incident, incidentType: IncidentType?, issuerName: String, application: Application) : AndroidViewModel(application) {

    private val SHARED_PREFS = "shared_prefs"

    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _status = MutableLiveData<IncidentApiStatus>()
    val status: LiveData<IncidentApiStatus>
        get() = _status

    private var _errorResponse = MutableLiveData<ApiErrorResponse>()
    val errorResponse : LiveData<ApiErrorResponse>
        get() = _errorResponse

    private var _incident = MutableLiveData<Incident?>()
    val incident : MutableLiveData<Incident?>
        get() = _incident


    private var _createdDateStr = MutableLiveData<String>()
    val createdDateStr : MutableLiveData<String>
        get() = _createdDateStr

    private var _updatedDateStr = MutableLiveData<String>()
    val updatedDateStr : MutableLiveData<String>
        get() = _updatedDateStr

    private var _statusStr = MutableLiveData<String>()
    val statusStr : MutableLiveData<String>
        get() = _statusStr

    private var _issuerName = MutableLiveData<String>()
    val issuerName : MutableLiveData<String>
        get() = _issuerName

    private var _incidentType = MutableLiveData<IncidentType>()
    val incidentType : MutableLiveData<IncidentType>
        get() = _incidentType

    init {
        _incident.value = null
        _issuerName.value = issuerName
        _incident.value = incident
        incidentType.let {
            _incidentType.value = incidentType!!

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

