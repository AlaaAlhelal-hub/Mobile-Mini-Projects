package com.example.manageincidentsapp.incident.editIncidentStatus

data class ChangeStatusRequest(
    val incidentId: String,
    val status: Int
)