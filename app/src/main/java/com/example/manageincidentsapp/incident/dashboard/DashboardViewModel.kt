package com.example.manageincidentsapp.incident.dashboard

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.manageincidentsapp.incident.Incident
import com.example.manageincidentsapp.incident.listOfIncident.ListOfIncidentResponse
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

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
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


    private var _dashboardData = MutableLiveData<List<IncidentStatistics>?>()
    val dashboardData : MutableLiveData<List<IncidentStatistics>?>
        get() = _dashboardData

    init {
        getDashboard()
    }

    fun getDashboard(){
        val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null).toString()

        coroutineScope.launch {
            _status.value = IncidentApiStatus.Pending

            IncidentApi.retrofitService.getDashboard(token).enqueue( object:
                Callback<DashboardResponse> {
                override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                    _status.value = IncidentApiStatus.Failure
                    _dashboardData.value = null
                    _errorResponse.value = ApiErrorResponse(null)
                    Log.i("Error ", "${t.message}")
                }

                override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                    Log.i("response code ", "${response.code()}")

                    if (response.code() == 200) {
                        _dashboardData.value = response.body()?.incidents
                        _status.value = IncidentApiStatus.Done
                    }
                    else if (response.code() == 403) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _dashboardData.value = null
                    }
                    else if (response.code() == 401) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _dashboardData.value = null
                    }
                }
            })

        }
    }


}