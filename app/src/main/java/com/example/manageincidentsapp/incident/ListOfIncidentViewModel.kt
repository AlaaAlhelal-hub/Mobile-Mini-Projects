package com.example.manageincidentsapp.incident


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.manageincidentsapp.user.ErrorHandler
import com.example.manageincidentsapp.user.IncidentApi
import com.example.manageincidentsapp.user.IncidentApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListOfIncidentViewModel(application: Application) : AndroidViewModel(application) {
    private val SHARED_PREFS = "shared_prefs"

    private val appContext = application
    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _status = MutableLiveData<IncidentApiStatus>()
    val status: LiveData<IncidentApiStatus>
        get() = _status


    private var _errorHandler = MutableLiveData<ErrorHandler>()
    val errorHandler : LiveData<ErrorHandler>
        get() = _errorHandler


    private var _listOfIncidents = MutableLiveData<Array<Incident>>()
    val listOfIncidents : LiveData<Array<Incident>>
        get() = _listOfIncidents


    private var _navigateToIncidentDetails = MutableLiveData<String>()
    val navigateToIncidentDetails: LiveData<String>
        get() = _navigateToIncidentDetails

    fun onIncidentClicked(id: String) {
        _navigateToIncidentDetails.value = id
    }

    fun onIncidentDetailsNavigated() {
        _navigateToIncidentDetails.value = null
    }


    init {
        getListOfIncidents()
    }

    private fun getListOfIncidents() {
        val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("TOKEN", null).toString()
        Log.i("token: ", token)
        token = "Bearer $token"
        val startDate = "2021-11-14"
        coroutineScope.launch {
            _status.value = IncidentApiStatus.InProgress

            IncidentApi.retrofitService.getListOfIncident(token, startDate).enqueue( object: Callback<ListOfIncidentResponse> {
                override fun onFailure(call: Call<ListOfIncidentResponse>, t: Throwable) {
                    _status.value = IncidentApiStatus.Rejected
                    _listOfIncidents.value = null
                    _errorHandler.value = ErrorHandler(null)
                    Log.i("Error ", "${t.message}")
                }

                override fun onResponse(call: Call<ListOfIncidentResponse>, response: Response<ListOfIncidentResponse>) {
                    Log.i("Error ", "${response.code()}")

                    if (response.code() == 200) {
                        _listOfIncidents.value = response.body()?.incidents
                        _status.value = IncidentApiStatus.Completed
                    }
                    else if (response.code() == 403) {
                        _errorHandler.value = ErrorHandler("you are not authorized")
                        _status.value = IncidentApiStatus.Rejected
                        _listOfIncidents.value = null
                    }
                    else if (response.code() == 401) {
                        _errorHandler.value = ErrorHandler("you are not authorized")
                        _status.value = IncidentApiStatus.Rejected
                        _listOfIncidents.value = null
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
