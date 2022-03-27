package com.example.manageincidents.domain.models

import java.io.Serializable

data class IncidentType(
    val arabicName: String,
    val englishName: String,
    val id: Int,
    val subTypes: List<SubIncidentType>
): Serializable

data class SubIncidentType(
    val arabicName: String,
    val categoryId: Int,
    val englishName: String,
    val id: Int
): Serializable
