package com.example.manageincidentsapp.incident.addNewIncident

import com.google.gson.annotations.SerializedName

data class IncidentRequest(
    @SerializedName("description")
    val description :String?,
    @SerializedName("typeId")
    val typeId: Int?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)
