/*
package com.example.manageincidents.presentaion.app.viewIncidentDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType

class ViewIncidentViewModelFactory (
    private val incident: Incident,
    private val incidentType: IncidentType?,
    private val issuerName: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewIncidentViewModel::class.java)) {
            return ViewIncidentViewModel(incident, incidentType, issuerName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

 */
