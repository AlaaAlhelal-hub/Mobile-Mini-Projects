package com.example.manageincidentsapp.media

data class Media(
    var id: String,
    var mimeType: String,
    var url: String,
    var type: Int,
    var incidentId: String
)
