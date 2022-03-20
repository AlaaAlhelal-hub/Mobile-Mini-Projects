package com.example.manageincidentsapp.incident.listOfIncident

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manageincidentsapp.R
import com.example.manageincidentsapp.databinding.FragmentListOfIncidentBinding
import com.example.manageincidentsapp.incident.IncidentAdapter
import com.example.manageincidentsapp.incident.IncidentListener
import com.example.manageincidentsapp.incident.addNewIncident.AddNewIncident
import com.example.manageincidentsapp.incidentType.IncidentType


class ListOfIncidentsFragment : Fragment() {
    lateinit var listOfIncidentsViewModel: ListOfIncidentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentListOfIncidentBinding>(inflater, R.layout.fragment_list_of_incident, container, false)

        listOfIncidentsViewModel = ViewModelProvider(this).get(ListOfIncidentViewModel::class.java)

        binding.lifecycleOwner = this

        binding.listOfIncidentsViewModel = listOfIncidentsViewModel


        val manager =  LinearLayoutManager(this.context)
        binding.listOfIncidents.layoutManager = manager

        val adapter = IncidentAdapter(IncidentListener { incident ->
            listOfIncidentsViewModel.onIncidentClicked(incident)
        })

        binding.listOfIncidents.adapter = adapter

        listOfIncidentsViewModel.listOfIncidents.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toMutableList())
            }
        })

        listOfIncidentsViewModel.nameListUpdated.observe(viewLifecycleOwner, Observer { newnameListUpdated ->
            if (newnameListUpdated) {
                listOfIncidentsViewModel.onNameListUpdatedFinished()
            }
        })

        listOfIncidentsViewModel.loadingStatus.observe(viewLifecycleOwner, Observer { newStatus ->
            if (newStatus) {
                binding.progressLayout.visibility = View.VISIBLE
            }
            else {
                binding.progressLayout.visibility = View.GONE
            }

        })


        binding.addNewIncident.setOnClickListener { view ->
            val intentAddNewIncident = Intent(activity, AddNewIncident::class.java)
            startActivity(intentAddNewIncident)
        }

        listOfIncidentsViewModel.navigateToIncidentDetails.observe(viewLifecycleOwner, Observer { incident ->
            incident?.let {

                var type: IncidentType? = null
                var issuerName = ""
                for (i in 0 until listOfIncidentsViewModel.incidentType.value!!.size){
                    if (listOfIncidentsViewModel.incidentType.value!![i].id == incident.typeId){
                        type = listOfIncidentsViewModel.incidentType.value!![i]
                        break
                    }
                }

                for (i in 0 until listOfIncidentsViewModel.listOfIncidents.value!!.size){
                    if (listOfIncidentsViewModel.listOfIncidents.value!![i].id == incident.id){
                        issuerName = listOfIncidentsViewModel.issuerNames.value!![i]
                        break
                    }
                }

                this.findNavController().navigate( ListOfIncidentsFragmentDirections.actionListOfIncidentsFragmentToIncidentDetailsFragment(
                        incident,
                        type,
                        issuerName
                    )
                )

            }

        })



        return binding.root
    }


}