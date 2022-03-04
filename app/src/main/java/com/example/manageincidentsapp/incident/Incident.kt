package com.example.manageincidentsapp.incident

import com.example.manageincidentsapp.media.Media

data class Incident (
    var id: String,
    var description: String,
    var latitude: Float,
    var longitude: Float,
    var status: Int,
    var priority: String?,
    var typeId: Int,
    val issuerId: String,
    var assigneeId: String?,
    var createdAt: String, //Date
    var updatedAt: String, //Date
    var medias: List<Media>?
)