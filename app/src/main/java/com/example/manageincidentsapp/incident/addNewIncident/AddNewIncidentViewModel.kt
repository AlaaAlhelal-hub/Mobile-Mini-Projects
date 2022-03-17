package com.example.manageincidentsapp.incident.addNewIncident

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.manageincidentsapp.incident.Incident
import com.example.manageincidentsapp.incidentType.IncidentType
import com.example.manageincidentsapp.incidentType.SubIncidentType
import com.example.manageincidentsapp.network.ApiErrorResponse
import com.example.manageincidentsapp.network.IncidentApi
import com.example.manageincidentsapp.user.IncidentApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File



class AddNewIncidentViewModel(application: Application) : AndroidViewModel(application) {

    private val SHARED_PREFS = "shared_prefs"

    private val appContext = application
    private val viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _incidentType = MutableLiveData<List<IncidentType>?>()
    val incidentType: MutableLiveData<List<IncidentType>?>
        get() = _incidentType

    private var _incidentSubtype = MutableLiveData<List<SubIncidentType>>()
    val incidentSubtype: LiveData<List<SubIncidentType>>
        get() = _incidentSubtype

    private var _getTypeStatus = MutableLiveData<IncidentApiStatus>()
    val getTypeStatus: LiveData<IncidentApiStatus>
        get() = _getTypeStatus


    private var _status = MutableLiveData<IncidentApiStatus>()
    val status: LiveData<IncidentApiStatus>
        get() = _status


    private var _errorResponse = MutableLiveData<ApiErrorResponse>()
    val errorResponse : LiveData<ApiErrorResponse>
        get() = _errorResponse


    private var _incidentId = MutableLiveData<String?>()
    val incidentId : MutableLiveData<String?>
        get() = _incidentId

    private var _newIncident = MutableLiveData<Incident>()
    val newIncident : LiveData<Incident>
        get() = _newIncident

    private var _uploadStatus = MutableLiveData<IncidentApiStatus>()
    val uploadStatus: LiveData<IncidentApiStatus>
        get() = _uploadStatus

    init {
        getIncidentType()
    }

    private fun getIncidentType() {
        val sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("TOKEN", null).toString()


        coroutineScope.launch {
            _status.value = IncidentApiStatus.Pending

            IncidentApi.retrofitService.getIncidentType(token).enqueue( object:
                Callback<List<IncidentType>> {
                override fun onResponse( call: Call<List<IncidentType>>, response: Response<List<IncidentType>>
                ) {
                    Log.i("response code ", "${response.code()}")

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

    fun postNewIncident(description: String, typeId: Int, latitude: Double, longitude: Double) {
        val sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("TOKEN", null).toString()
        Log.i("token: ", token)

        coroutineScope.launch {
            _status.value = IncidentApiStatus.Pending
            val incidentRequest = IncidentRequest(description, typeId, latitude, longitude)
            IncidentApi.retrofitService.postNewIncident(token, incidentRequest).enqueue( object:
                Callback<PostIncidentResponse> {
                override fun onResponse(call: Call<PostIncidentResponse>, response: Response<PostIncidentResponse>
                ) {
                    Log.i("response code ", "${response.code()}")

                    if (response.code() == 200) {
                        _incidentId.value = response.body()?.id
                        Log.i("response body ", "${_incidentId.value.toString()}")

                        _status.value = IncidentApiStatus.Done
                    }
                    else if (response.code() == 403) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _incidentId.value = null
                    }
                    else if (response.code() == 401) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _status.value = IncidentApiStatus.Failure
                        _incidentId.value = null
                    }
                }

                override fun onFailure(call: Call<PostIncidentResponse>, t: Throwable) {
                    _status.value = IncidentApiStatus.Failure
                    _incidentId.value = null
                    _errorResponse.value = ApiErrorResponse(null)
                    Log.i("Error ", "${t.message}")                }

            })
        }
    }//

    fun uploadIncidentPhotos(id: String, imageUri: Uri){


        val sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null).toString()

        val file = File(imageUri.path)
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        val extension = mime.getExtensionFromMimeType(appContext.getContentResolver().getType(imageUri));

        coroutineScope.launch {

            val requestBody = RequestBody.create(MediaType.parse("image/$extension"), "$file.$extension")

            val filePart = MultipartBody.Part.createFormData(
                "image",
                "${file.name}.$extension",
                requestBody
            )

            _uploadStatus.value = IncidentApiStatus.Pending

            IncidentApi.retrofitService.uploadIncidentPhotos(token, id ,filePart).enqueue( object:
                Callback<Void> {
                override fun onResponse( call: Call<Void>, response: Response<Void>
                ) {
                    Log.i("response code ", "${response.code()}")

                    if (response.code() == 200) {
                        _uploadStatus.value = IncidentApiStatus.Done
                    }
                    else if (response.code() == 403) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _uploadStatus.value = IncidentApiStatus.Failure
                    }
                    else if (response.code() == 401) {
                        _errorResponse.value = ApiErrorResponse("you are not authorized")
                        _uploadStatus.value = IncidentApiStatus.Failure
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    _uploadStatus.value = IncidentApiStatus.Failure
                    _errorResponse.value = ApiErrorResponse(null)
                    Log.i("Error ", "${t.message}")                }

            })
        }


    }


}