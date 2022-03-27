package com.example.manageincidents.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Media(
    @SerializedName("id") var id: String,
    @SerializedName("mimeType") var mimeType: String,
    @SerializedName("url") var url: String,
    @SerializedName("type") var type: Int,
    @SerializedName("incidentId") var incidentId: String
): Serializable
