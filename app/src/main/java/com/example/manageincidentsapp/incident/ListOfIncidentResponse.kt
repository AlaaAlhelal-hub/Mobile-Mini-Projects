package com.example.manageincidentsapp.incident

import com.google.gson.annotations.SerializedName

data class ListOfIncidentResponse (
    val baseURL: String,
    @SerializedName("incidents")
    val incidents: Array<Incident>
)