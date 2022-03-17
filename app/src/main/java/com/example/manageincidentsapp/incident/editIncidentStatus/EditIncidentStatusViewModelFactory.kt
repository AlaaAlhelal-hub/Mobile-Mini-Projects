package com.example.manageincidentsapp.incident.editIncidentStatus

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidentsapp.incidentType.IncidentType

class EditIncidentStatusViewModelFactory(
    private val incidentId: String,
    private var currentStatus: Int,
    private var incidentType: IncidentType,
    private var issuerName: String,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditIncidentStatusViewModel::class.java)) {

            return EditIncidentStatusViewModel(incidentId, currentStatus, incidentType, issuerName, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}