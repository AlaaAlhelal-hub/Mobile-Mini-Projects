package com.example.manageincidentsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.manageincidentsapp.incident.IncidentAdapter
import com.example.manageincidentsapp.incident.IncidentListener
import com.example.manageincidentsapp.incident.ListOfIncidentViewModel
import com.example.manageincidentsapp.databinding.ActivityListOfIncidentsBinding

class ListOfIncidents : AppCompatActivity() {

    lateinit var listOfIncidentsViewModel: ListOfIncidentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListOfIncidentsBinding = ActivityListOfIncidentsBinding.inflate(layoutInflater)

        listOfIncidentsViewModel = ViewModelProvider(this).get(ListOfIncidentViewModel::class.java)


        binding.lifecycleOwner = this

        binding.listOfIncidentsViewModel = listOfIncidentsViewModel


        val manager = GridLayoutManager(this, 2)
        binding.listOfIncidents.layoutManager = manager

        val adapter = IncidentAdapter(IncidentListener { incidentId ->
            listOfIncidentsViewModel.onIncidentClicked(incidentId)
        })

        binding.listOfIncidents.adapter = adapter

        listOfIncidentsViewModel.listOfIncidents.observe(this, Observer {
            it?.let {
                adapter.submitList(it.toMutableList())
            }
        })
        setContentView(binding.root)

    }



}