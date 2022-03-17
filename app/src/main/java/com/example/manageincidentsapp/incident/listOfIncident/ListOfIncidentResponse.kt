package com.example.manageincidentsapp.incident.listOfIncident

import com.example.manageincidentsapp.incident.Incident
import com.google.gson.annotations.SerializedName

data class ListOfIncidentResponse (
    val baseURL: String,
    @SerializedName("incidents")
    val incidents: Array<Incident>
)