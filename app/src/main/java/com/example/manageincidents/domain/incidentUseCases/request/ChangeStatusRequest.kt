package com.example.manageincidents.domain.incidentUseCases.request

data class ChangeStatusRequest(
    val incidentId: String,
    val status: Int
)