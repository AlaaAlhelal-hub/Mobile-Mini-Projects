package com.example.manageincidentsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.manageincidentsapp.databinding.ActivityListOfIncidentsBinding
import com.example.manageincidentsapp.incident.*

class ListOfIncidents : AppCompatActivity() {

    lateinit var listOfIncidentsViewModel: ListOfIncidentViewModel
    lateinit var incidentsViewModel: ViewIncidentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListOfIncidentsBinding = ActivityListOfIncidentsBinding.inflate(layoutInflater)

        /*
        incidentsViewModel = ViewModelProvider(this).get(ViewIncidentViewModel::class.java)

        listOfIncidentsViewModel = ViewModelProvider(this).get(ListOfIncidentViewModel::class.java)

        listOfIncidentsViewModel.navigateToIncidentDetails.observe(this, Observer { newIncident ->
            Log.i("observe value of incident","${newIncident.toString()}")
            incidentsViewModel.incident.value = newIncident
        })
*/

        setContentView(binding.root)

    }




}