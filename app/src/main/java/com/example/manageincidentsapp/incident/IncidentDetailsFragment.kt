package com.example.manageincidentsapp.incident

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.manageincidentsapp.R
import com.example.manageincidentsapp.databinding.FragmentIncidentDetailsBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class IncidentDetailsFragment : Fragment() {

    private lateinit var incidentsViewModel: ViewIncidentViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentIncidentDetailsBinding>(inflater, R.layout.fragment_incident_details, container, false)


        val arguments = IncidentDetailsFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ViewIncidentViewModelFactory(arguments.incident, arguments.incidentType, arguments.issuerName , requireActivity().application)

        incidentsViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(ViewIncidentViewModel::class.java)


        incidentsViewModel.incident.observe(viewLifecycleOwner, Observer { newIncident ->


            when(newIncident?.status) {
                0 -> { incidentsViewModel.statusStr.value = IncidentStatus.Submitted.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_submitted)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Submitted)}
                1 -> { incidentsViewModel.statusStr.value = IncidentStatus.InProgress.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_in_progress)
                    binding.incidentStatus.setTextAppearance(R.style.Status_InProgress)}
                2 -> { incidentsViewModel.statusStr.value = IncidentStatus.Completed.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_completed)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Completed)}
                3 -> { incidentsViewModel.statusStr.value = IncidentStatus.Rejected.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_rejected)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Rejected)}
            }

        })
        binding.incidentData = incidentsViewModel

        binding.changeIncidentStatus.setOnClickListener {
            this.findNavController().navigate(
                IncidentDetailsFragmentDirections
                    .actionIncidentDetailsFragmentToEditIncidentStatusFragment(incidentsViewModel.incident.value!!.id, incidentsViewModel.incident.value!!.status,
                         incidentsViewModel.issuerName.value!!, incidentsViewModel.incidentType.value!!))
        }

        return binding.root
    }



}