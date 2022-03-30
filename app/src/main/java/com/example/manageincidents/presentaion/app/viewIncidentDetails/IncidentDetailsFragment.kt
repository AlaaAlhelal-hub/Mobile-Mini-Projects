package com.example.manageincidents.presentaion.app.viewIncidentDetails

import android.R.attr.defaultValue
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.manageincidents.R
import com.example.manageincidents.data.IncidentStatus
import com.example.manageincidents.databinding.FragmentIncidentDetailsBinding
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IncidentDetailsFragment : Fragment() {

    var snackbar: Snackbar? = null

    private lateinit var viewModel: ViewIncidentViewModel

    lateinit var binding: FragmentIncidentDetailsBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentIncidentDetailsBinding>(inflater,
            R.layout.fragment_incident_details, container, false)


       // val arguments =IncidentDetailsFragmentArgs.fromBundle( requireArguments())
       // val viewModelFactory = ViewIncidentViewModelFactory(arguments.incident, arguments.incidentType, arguments.issuerName)

        viewModel = ViewModelProvider(this)
            .get(ViewIncidentViewModel::class.java)


        val incident: Incident
        val incidentType: IncidentType
        val issuerName: String
        val bundle = requireArguments()

        incident = bundle.getSerializable("incident") as Incident
        incidentType = bundle.getSerializable("type") as IncidentType
        issuerName = bundle.getString("issuerName")!!
        viewModel.incident.value = incident
        viewModel.incidentType.value = incidentType
        viewModel.issuerName.value = issuerName

        viewModel.incident.observe(viewLifecycleOwner, Observer { newIncident ->
            when(newIncident?.status) {
                0 -> { viewModel.statusStr.value = IncidentStatus.Submitted.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_submitted)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Submitted)}
                1 -> { viewModel.statusStr.value = IncidentStatus.InProgress.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_in_progress)
                    binding.incidentStatus.setTextAppearance(R.style.Status_InProgress)}
                2 -> { viewModel.statusStr.value = IncidentStatus.Completed.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_completed)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Completed)}
                3 -> { viewModel.statusStr.value = IncidentStatus.Rejected.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_rejected)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Rejected)}
            }

        })

        binding.incidentData = viewModel

        binding.changeIncidentStatus.setOnClickListener {

            bundle.putString("incidentId", viewModel.incident.value!!.id)
            bundle.putInt("incidentStatus", viewModel.incident.value!!.status)

            this.findNavController().navigate(R.id.action_incidentDetailsFragment_to_editIncidentStatusFragment, bundle
               /* viewModel.incident.value!!.id,
                viewModel.incident.value!!.status,
                viewModel.issuerName.value!!,
                viewModel.incidentType.value!!
                */
            )
        }

        return binding.root
    }

    fun showSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG, @StringRes actionText: Int = R.string.dismiss, @ColorRes actionTextColor: Int= R.color.primaryDarkColor, action: () -> Unit = {}) {
        snackbar = requireActivity().findViewById<View>(android.R.id.content)?.let { Snackbar.make(it, message, length) }
        snackbar?.setAction(getString(actionText)) {
            action()
            snackbar?.dismiss()
        }
        snackbar?.setActionTextColor(ContextCompat.getColor( requireActivity().applicationContext, actionTextColor))
        snackbar?.show()
    }

}