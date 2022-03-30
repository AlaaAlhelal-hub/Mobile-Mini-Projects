package com.example.manageincidents.presentaion.app.editIncidentStatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.manageincidents.R
import com.example.manageincidents.data.utils.ApiStatus
import com.example.manageincidents.databinding.FragmentEditIncidentStatusBinding
import com.example.manageincidents.domain.models.Incident
import com.example.manageincidents.domain.models.IncidentType
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditIncidentStatusFragment : Fragment() {


    private lateinit var viewModel: EditIncidentStatusViewModel
    var snackbar: Snackbar? = null

    lateinit var binding: FragmentEditIncidentStatusBinding
    private var selectedStatus = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_edit_incident_status, container, false)

        //val arguments = EditIncidentStatusFragmentArgs.fromBundle(requireArguments())

       // viewModelFactory = EditIncidentStatusViewModelFactory(arguments.incidentId,
         //   arguments.currentStatus,  arguments.incidentType, arguments.issuerName)


        viewModel =  ViewModelProvider(this).get(
            EditIncidentStatusViewModel::class.java)

        val incidentId: String
        val incidentType: IncidentType
        val issuerName: String
        val currentStatus: Int
        val bundle = requireArguments()
        incidentId = bundle.getString("incidentId")!!
        incidentType = bundle.getSerializable("type") as IncidentType
        issuerName = bundle.getString("issuerName")!!
        currentStatus = bundle.getInt("incidentStatus")
        viewModel.incidentIdValue.value = incidentId
        viewModel.incidentTypeValue.value = incidentType
        viewModel.issuerNameValue.value = issuerName
        viewModel.currentStatusValue.value = currentStatus


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
            viewModel.changeStatus(selectedStatus)
        }

        viewModel.status.observe(viewLifecycleOwner, Observer { newStatus ->
            if (newStatus == ApiStatus.Done){

                this.findNavController().navigate(EditIncidentStatusFragmentDirections.actionEditIncidentStatusFragmentToIncidentDetailsFragment(
                     /*   viewModel.incident.value!!,
                        viewModel.incidentTypeValue.value,
                        viewModel.issuerNameValue.value!!
                    */)
                )
            }

        })

        viewModel.responseError.observe(viewLifecycleOwner, Observer {
            if (it.errorFlag) {
                showSnackBar(it.message.toString(), Snackbar.LENGTH_LONG)
            }
        })

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