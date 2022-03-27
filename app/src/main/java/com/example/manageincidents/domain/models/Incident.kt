package com.example.manageincidents.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Incident(
    @SerializedName("id") val id: String,
    @SerializedName("assigneeId") val assigneeId: Any,
    @SerializedName("issuerId") val issuerId: String,
    @SerializedName("createdAt") var createdAt: String,
    @SerializedName("updatedAt") var updatedAt: String,
    @SerializedName("description") val description: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("medias") val medias: List<Any>,
    @SerializedName("priority") val priority: Any,
    @SerializedName("status") val status: Int,
    @SerializedName("typeId") val typeId: Int
): Serializable