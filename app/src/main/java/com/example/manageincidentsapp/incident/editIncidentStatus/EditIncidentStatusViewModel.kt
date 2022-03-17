package com.example.manageincidentsapp.incident.editIncidentStatus

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.manageincidentsapp.incident.Incident
import com.example.manageincidentsapp.incidentType.IncidentType
import com.example.manageincidentsapp.network.ApiErrorResponse
import com.example.manageincidentsapp.network.IncidentApi
import com.example.manageincidentsapp.user.IncidentApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditIncidentStatusViewModel(incidentId: String, currentStatus: Int, incidentType: IncidentType, issuerName: String, application: Application) : AndroidViewModel(application) {

    private val SHARED_PREFS = "shared_prefs"
    private var appContext: Application = application
    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _status = MutableLiveData<IncidentApiStatus>()
    val status: LiveData<IncidentApiStatus>
        get() = _status

    private var _errorResponse = MutableLiveData<ApiErrorResponse>()
    val errorResponse : LiveData<ApiErrorResponse>
        get() = _errorResponse

    private var _incidentId = MutableLiveData<String>()
    val incidentId : MutableLiveData<String>
        get() = _incidentId

    private var _incident = MutableLiveData<Incident?>()
    val incident : MutableLiveData<Incident?>
        get() = _incident

    private var _incidentType = MutableLiveData<IncidentType?>()
    val incidentType : MutableLiveData<IncidentType?>
        get() = _incidentType

    private var _issuerName = MutableLiveData<String?>()
    val issuerName : MutableLiveData<String?>
        get() = _issuerName

    private var _currentStatus = MutableLiveData<Int>()
    val currentStatus : MutableLiveData<Int>
        get() = _currentStatus

    init {
        _issuerName.value = issuerName
        _incidentType.value = incidentType
        _incidentId.value = incidentId
        _currentStatus.value = currentStatus
    }


    fun changeStatus(newStatus: Int) {
        val sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null).toString()
        Log.i("token: ", token)


        coroutineScope.launch {
            _status.value = IncidentApiStatus.Pending

            IncidentApi.retrofitService.changeStatus(token, ChangeStatusRequest(_incidentId.value!!, newStatus)).enqueue( object:
                Callback<Incident> {
                override fun onResponse(call: Call<Incident>, response: Response<Incident>
                ) {
                    Log.i("response code ", "${response.code()}")

                    if (response.code() == 200) {
                        _incident.value = response.body()
                        _status.value = IncidentApiStatus.Done
                    }
                    else if (response.code() == 403) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _incident.value = null
                        Toast.makeText(appContext,"you are unauthorized", Toast.LENGTH_LONG).show()
                    }
                    else if (response.code() == 401) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _incident.value = null
                    }
                }

                override fun onFailure(call: Call<Incident>, t: Throwable) {
                    _status.value = IncidentApiStatus.Failure
                    _incident.value = null
                    _errorResponse.value = ApiErrorResponse(null)
                    Log.i("Error ", "${t.message}")                }

            })
        }
    }//



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
