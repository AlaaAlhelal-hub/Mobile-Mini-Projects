package com.example.manageincidents.presentaion.app.viewListOfIncident

import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.User

sealed class ListOfIncidentEvent {
    data class OnError(val message: String) : ListOfIncidentEvent()
    data class ListOfIncidents(val listOfIncidents: List<Incident>): ListOfIncidentEvent()
    data class ListOfUsers(val listOfUsers: List<User>): ListOfIncidentEvent()
    data class IncidentTypes(val listOfTypes: List<IncidentType>): ListOfIncidentEvent()
    object NavigateToAddNewIncident : ListOfIncidentEvent()
    object NavigateToDashboard : ListOfIncidentEvent()
}