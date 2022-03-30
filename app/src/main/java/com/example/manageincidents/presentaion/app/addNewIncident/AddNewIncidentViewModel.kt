package com.example.manageincidents.presentaion.app.addNewIncident


import android.app.Application
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.*
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.domain.incidentUseCases.GetIncidentTypesUseCase
import com.example.manageincidents.domain.incidentUseCases.UploadIncidentsPhotosUseCase
import com.example.manageincidents.domain.incidentUseCases.PostNewIncidentUseCase
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.domain.models.SubIncidentType
import com.example.manageincidents.domain.utils.ResponseError
import com.example.manageincidents.presentaion.base.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class AddNewIncidentViewModel @Inject constructor(
    private val postNewIncidentUseCase: PostNewIncidentUseCase,
    private val getIncidentTypesUseCase: GetIncidentTypesUseCase,
    private val uploadIncidentsPhotosUseCase: UploadIncidentsPhotosUseCase
) : ViewModel() {


    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus


    private var _incidentType = MutableLiveData<List<IncidentType>>()
    val incidentType: MutableLiveData<List<IncidentType>>
        get() = _incidentType

    private var _incidentSubtype = MutableLiveData<List<SubIncidentType>>()
    val incidentSubtype: MutableLiveData<List<SubIncidentType>>
        get() = _incidentSubtype

    private var _getTypeStatus = MutableLiveData<ApiStatus>()
    val getTypeStatus: MutableLiveData<ApiStatus>
        get() = _getTypeStatus


    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
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

    private var _uploadStatus = MutableLiveData<ApiStatus>()
    val uploadStatus: LiveData<ApiStatus>
        get() = _uploadStatus

    var responseError = MutableLiveData<ResponseError>()


    init {
        getIncidentType()
    }


    private fun getIncidentType() {
        viewModelScope.launch {
            _getTypeStatus.value = ApiStatus.Pending
            _loadingStatus.value = true
            //try {
                when (val response = getIncidentTypesUseCase.invoke(GetIncidentTypesUseCase.Request())) {
                    is ResultWrapper.Success -> {
                        _getTypeStatus.value = ApiStatus.Done
                        _loadingStatus.value = false
                        _incidentType.value = response.data!!
                    }
                    is ResultWrapper.Error.ApiError -> {
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError(response.errorMessage, true)
                    }
                    is ResultWrapper.Error.TimeoutError -> {
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError("TimeOut Error", true)
                    }
                    is ResultWrapper.Error.ClientConnectionError -> {
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Connection Error", true)
                    }
                    is ResultWrapper.Error.UnexpectedError ->{
                        _getTypeStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Unexpected Error", true)
                    }
                }
           // } catch (exception: Exception) {
             //   Log.i("get Incident Type ", exception.toString())
           // }
        }
    }


    fun postNewIncident(description: String, typeId: Int, latitude: Double, longitude: Double) {
       viewModelScope.launch {

           _status.value = ApiStatus.Pending
           _loadingStatus.value = true
           try {
               when (val response = postNewIncidentUseCase.invoke(PostNewIncidentUseCase.Request(description, latitude, longitude, typeId))) {
                   is ResultWrapper.Success -> {
                       _status.value = ApiStatus.Done
                       _loadingStatus.value = false
                       _incidentId.value = response.data.id
                   }
                   is ResultWrapper.Error.ApiError -> {
                       _status.value = ApiStatus.Failure
                       _loadingStatus.value = false
                       responseError.value = ResponseError(response.errorMessage, true)
                   }
                   is ResultWrapper.Error.TimeoutError -> {
                       _status.value = ApiStatus.Failure
                       _loadingStatus.value = false
                       responseError.value = ResponseError("TimeOut Error", true)
                   }
                   is ResultWrapper.Error.ClientConnectionError -> {
                       _status.value = ApiStatus.Failure
                       _loadingStatus.value = false
                       responseError.value =
                           ResponseError("Connection Error", true)
                   }
                   is ResultWrapper.Error.UnexpectedError ->{
                       _status.value = ApiStatus.Failure
                       _loadingStatus.value = false
                       responseError.value =
                           ResponseError("Unexpected Error", true)
                   }
               }
           } catch (exception: Exception) {
               Log.i("postNewIncident", "${exception.message}")
           }

        }
    }//

    fun uploadIncidentPhotos(id: String, imageUri: Uri){

        viewModelScope.launch {
            val file = File(imageUri.path)
            val mime: MimeTypeMap = MimeTypeMap.getSingleton()
            val extension = mime.getExtensionFromMimeType(Application().applicationContext.contentResolver.getType(imageUri));
            val requestBody = RequestBody.create(MediaType.parse("image/$extension"), "$file.$extension")
            val filePart = MultipartBody.Part.createFormData(
                "image",
                "${file.name}.$extension",
                requestBody
            )
            _uploadStatus.value = ApiStatus.Pending
            _loadingStatus.value = true
            try {
                when (val response = uploadIncidentsPhotosUseCase.invoke(UploadIncidentsPhotosUseCase.Request(id, filePart))) {
                    is ResultWrapper.Success -> {
                        _uploadStatus.value = ApiStatus.Done
                        _loadingStatus.value = false
                    }
                    is ResultWrapper.Error.ApiError -> {
                        _uploadStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError(response.errorMessage, true)
                    }
                    is ResultWrapper.Error.TimeoutError -> {
                        _uploadStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value = ResponseError("TimeOut Error", true)
                    }
                    is ResultWrapper.Error.ClientConnectionError -> {
                        _uploadStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Connection Error", true)
                    }
                    is ResultWrapper.Error.UnexpectedError ->{
                        _uploadStatus.value = ApiStatus.Failure
                        _loadingStatus.value = false
                        responseError.value =
                            ResponseError("Unexpected Error", true)
                    }
                }
            } catch (exception: Exception) {
                Log.i("uploadIncidentPhotos ", "$exception")
            }


        }


    }


}