package com.example.manageincidents.presentaion.app.viewListOfIncident

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.manageincidents.R
import com.example.manageincidents.data.IncidentStatus
import com.example.manageincidents.databinding.IncidentItemBinding
import com.example.manageincidents.domain.models.Incident


class IncidentAdapter (val clickListener: IncidentListener): ListAdapter<Incident, IncidentAdapter.ViewHolder>(
    IncidentDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(clickListener,item)
    }


    class ViewHolder private constructor(val binding: IncidentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: IncidentListener, item: Incident) {
            binding.incident = item
            binding.clickListener = clickListener

            when(binding.incident?.status) {
                0 -> { binding.incidentStatus.text = IncidentStatus.Submitted.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_submitted)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Submitted)}
                1 -> { binding.incidentStatus.text = IncidentStatus.InProgress.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_in_progress)
                    binding.incidentStatus.setTextAppearance(R.style.Status_InProgress)}
                2 -> { binding.incidentStatus.text = IncidentStatus.Completed.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_completed)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Completed)}
                3 -> { binding.incidentStatus.text = IncidentStatus.Rejected.toString()
                    binding.statusIcon.setImageResource(R.drawable.ic_circle_rejected)
                    binding.incidentStatus.setTextAppearance(R.style.Status_Rejected)}
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IncidentItemBinding.inflate(layoutInflater, parent, false)



                return ViewHolder(binding)
            }
        }
    }
}

class IncidentDiffCallback: DiffUtil.ItemCallback<Incident>(){
    override fun areItemsTheSame(oldItem: Incident, newItem: Incident): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Incident, newItem: Incident): Boolean {
        return oldItem == newItem
    }
}

class IncidentListener(val clickListener: (incident: Incident) -> Unit) {
    fun onClick(incident: Incident) = clickListener(incident)
}

