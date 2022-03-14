package com.example.manageincidentsapp.incident

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidentsapp.incidentType.IncidentType

class ViewIncidentViewModelFactory (
    private val incident: Incident,
    private val incidentType: IncidentType?,
    private val issuerName: String,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewIncidentViewModel::class.java)) {
            return ViewIncidentViewModel(incident, incidentType, issuerName, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}