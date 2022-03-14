package com.example.manageincidentsapp.incident

data class ChangeStatusRequest(
    val incidentId: String,
    val status: Int
)