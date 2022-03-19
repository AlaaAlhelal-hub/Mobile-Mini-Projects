package com.example.manageincidentsapp.incident.dashboard

data class DashboardResponse(
    val baseURL: String,
    val incidents: List<IncidentStatistics>
)