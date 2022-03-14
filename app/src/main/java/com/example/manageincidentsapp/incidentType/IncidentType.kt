package com.example.manageincidentsapp.incidentType

import java.io.Serializable

data class IncidentType(
    var id: Int,
    var arabicName: String,
    var englishName: String,
    var subTypes: List<SubIncidentType>?
): Serializable
