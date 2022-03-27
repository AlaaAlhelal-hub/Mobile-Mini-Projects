//package com.example.manageincidents.presentaion.app.editIncidentStatus
//
//import android.app.Application
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.manageincidents.domain.models.IncidentType
//
//class EditIncidentStatusViewModelFactory(
//    private val incidentId: String,
//    private var currentStatus: Int,
//    private var incidentType: IncidentType,
//    private var issuerName: String
//) : ViewModelProvider.Factory {
//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(EditIncidentStatusViewModel::class.java)) {
//
//            return EditIncidentStatusViewModel(incidentId, currentStatus, incidentType, issuerName, null) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
