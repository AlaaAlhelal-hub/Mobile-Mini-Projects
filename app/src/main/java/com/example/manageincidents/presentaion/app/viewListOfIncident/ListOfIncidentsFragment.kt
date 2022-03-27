package com.example.manageincidents.presentaion.app.viewListOfIncident

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manageincidents.R
import com.example.manageincidents.incident.IncidentAdapter
import com.example.manageincidents.incident.IncidentListener
import com.example.manageincidents.databinding.FragmentListOfIncidentBinding
import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.presentaion.app.addNewIncident.AddNewIncident
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListOfIncidentsFragment : Fragment(){



    private val viewModel: ListOfIncidentViewModel by lazy {
        ViewModelProvider(this).get(ListOfIncidentViewModel::class.java)
    }


    lateinit var binding: FragmentListOfIncidentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_of_incident, container, false)

        binding.lifecycleOwner = this

        binding.listOfIncidentsViewModel = viewModel


        val manager =  LinearLayoutManager(this.context)
        binding.listOfIncidents.layoutManager = manager

        val adapter = IncidentAdapter(IncidentListener { incident ->
            viewModel.onIncidentClicked(incident)
        })

        binding.listOfIncidents.adapter = adapter

        viewModel.listOfIncidents.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toMutableList())
            }
        })

        viewModel.nameListUpdated.observe(viewLifecycleOwner, Observer { newnameListUpdated ->
            if (newnameListUpdated) {
                viewModel.onNameListUpdatedFinished()
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer { newStatus ->
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

        viewModel.navigateToIncidentDetails.observe(viewLifecycleOwner, Observer { incident ->
            incident?.let {

                var type: IncidentType? = null
                var issuerName = ""
                for (i in 0 until viewModel.incidentType.value!!.size){
                    if (viewModel.incidentType.value!![i].id == incident.typeId){
                        type = viewModel.incidentType.value!![i]
                        break
                    }
                }

                for (i in 0 until viewModel.listOfIncidents.value!!.size){
                    if (viewModel.listOfIncidents.value!![i].id == incident.id){
                        issuerName = viewModel.issuerNames.value!![i]
                        break
                    }
                }


                savedInstanceState?.putSerializable("incident", incident)
                savedInstanceState?.putSerializable("type", type)
                savedInstanceState?.putString("issuerName", issuerName)
                this.setArguments(savedInstanceState)

                this.findNavController().navigate(ListOfIncidentsFragmentDirections
                    .actionListOfIncidentsFragmentToIncidentDetailsFragment(
                     /*
                        incident,
                        type,
                        issuerName
                */
                    )
                )

            }

        })



        return binding.root
    }


}