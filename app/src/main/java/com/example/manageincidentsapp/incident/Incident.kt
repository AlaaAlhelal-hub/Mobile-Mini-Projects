package com.example.manageincidentsapp.incident


import com.example.manageincidentsapp.media.Media
import java.io.Serializable
import java.util.*


data class Incident(
    var id: String,
    var description: String,
    var latitude: Double,
    var longitude: Double,
    var status: Int,
    var priority: Int?,
    var typeId: Int,
    val issuerId: String?,
    var assigneeId: String?,
    var createdAt: String,
    var updatedAt: String,
    var medias: List<Media>?
): Serializable