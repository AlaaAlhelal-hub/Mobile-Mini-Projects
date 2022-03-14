package com.example.manageincidentsapp.incident

enum class IncidentStatus {
    Submitted(0),
    InProgress(1),
    Completed(2),
    Rejected(3);

    var status: Int? = null

    constructor()

    constructor(
        status: Int
    ) {
        this.status = status
    }

}
