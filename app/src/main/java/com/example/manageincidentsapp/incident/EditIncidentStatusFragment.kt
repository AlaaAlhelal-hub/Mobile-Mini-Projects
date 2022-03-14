package com.example.manageincidentsapp.incident

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.manageincidentsapp.R
import com.example.manageincidentsapp.databinding.FragmentEditIncidentStatusBinding
import com.example.manageincidentsapp.user.IncidentApiStatus


class EditIncidentStatusFragment : Fragment() {

    private lateinit var incidentViewModel: EditIncidentStatusViewModel
    private lateinit var viewIncidentViewModel: ViewIncidentViewModel

    private var selectedStatus = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentEditIncidentStatusBinding>(inflater,
            R.layout.fragment_edit_incident_status, container, false)

        val arguments = EditIncidentStatusFragmentArgs.fromBundle(requireArguments())


        val viewModelFactory = EditIncidentStatusViewModelFactory(arguments.incidentId, arguments.currentStatus,  arguments.incidentType, arguments.issuerName,requireActivity().application)
        incidentViewModel =  ViewModelProvider(this, viewModelFactory).get(EditIncidentStatusViewModel::class.java)


        when (selectedStatus) {
            0 -> binding.submittedRadioButton.isChecked = true
            1 -> binding.inProgressRadioButton.isChecked = true
            2 -> binding.completedRadioButton.isChecked = true
            3 -> binding.rejectedRadioButton.isChecked = true
            else -> binding.submittedRadioButton.isChecked = true
        }

        binding.statusRadioGroup.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            selectedStatus = when (checkedId) {
                R.id.submitted_radio_button -> 0
                R.id.in_progress_radio_button -> 1
                R.id.completed_radio_button -> 2
                R.id.rejected_radio_button -> 3
                else -> -1
            }
        }

        binding.saveChanges.setOnClickListener {
            incidentViewModel.changeStatus(selectedStatus)
        }

        incidentViewModel.status.observe(viewLifecycleOwner, Observer { newStatus ->
            if (newStatus == IncidentApiStatus.Done){

                this.findNavController().navigate(
                    EditIncidentStatusFragmentDirections
                        .actionEditIncidentStatusFragmentToIncidentDetailsFragment(incidentViewModel.incident.value!!, incidentViewModel.incidentType.value , incidentViewModel.issuerName.value!!))
            }

        })
        return binding.root
    }



}