package com.example.manageincidents.presentaion.app.viewListOfIncident

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manageincidents.R
import com.example.manageincidents.databinding.FragmentIncidentDetailsBinding
import com.example.manageincidents.databinding.FragmentListOfIncidentBinding
import com.example.manageincidents.domain.models.IncidentType
import com.example.manageincidents.presentaion.app.addNewIncident.AddNewIncident
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ListOfIncidentsFragment : Fragment(){


/*
    private val viewModel: ListOfIncidentViewModel by lazy {
        ViewModelProvider(this)[ListOfIncidentViewModel::class.java]
    }
*/
 val viewModel: ListOfIncidentViewModel
        by viewModels()

    lateinit var binding: FragmentListOfIncidentBinding
    var snackbar: Snackbar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_of_incident, container, false)

        binding.lifecycleOwner = this

        binding.listOfIncidentsViewModel = viewModel


        val manager =  LinearLayoutManager(requireContext())
        binding.listOfIncidents.layoutManager = manager


        val adapter = IncidentAdapter(IncidentListener { incident ->
            viewModel.onIncidentClicked(incident)
        })
        binding.listOfIncidents.adapter = adapter


        viewModel.listOfIncidents.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toMutableList())
            }
            binding.listOfIncidents.adapter = adapter
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
                    }else {
                       for(j in 0 until viewModel.incidentType.value!![i].subTypes!!.size)
                           if ( viewModel.incidentType.value!![i].subTypes!![j].id == incident.typeId) {
                               type =  IncidentType(viewModel.incidentType.value!![i].subTypes!![j].arabicName, viewModel.incidentType.value!![i].subTypes!![j].englishName, viewModel.incidentType.value!![i].subTypes!![j].id, null)
                               break
                           }
                    }
                }

                for (i in 0 until viewModel.listOfIncidents.value!!.size){
                    if (viewModel.listOfIncidents.value!![i].id == incident.id){
                        issuerName = viewModel.issuerNames.value!![i]
                        break
                    }
                }

                val bundle = Bundle()
                bundle.putSerializable("incident", incident)
                bundle.putSerializable("type", type)
                bundle.putString("issuerName", issuerName)

                this.findNavController()
                    .navigate(R.id.action_listOfIncidentsFragment_to_incidentDetailsFragment, bundle)
                /*
                this.findNavController().navigate(ListOfIncidentsFragmentDirections
                    .actionListOfIncidentsFragmentToIncidentDetailsFragment(
                     /*
                        incident,
                        type,
                        issuerName
                */
                    ))
*/
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