package com.example.manageincidents.presentaion.app.viewIncidentDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class ViewIncidentViewModel @Inject constructor(/*
    incident: Incident,
    incidentType: IncidentType?,
    issuerName: String
    */) : ViewModel() {


    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
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
/*
    init {

        _incident.value = null
        _issuerName.value = issuerName
        _incident.value = incident
        incidentType.let {
            _incidentType.value = incidentType!!

        }
    }
*/


}

