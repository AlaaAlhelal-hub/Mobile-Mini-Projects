package com.example.manageincidentsapp.incident

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.manageincidentsapp.databinding.IncidentItemBinding


class IncidentAdapter (val clickListener: IncidentListener): ListAdapter<Incident, IncidentAdapter.ViewHolder>(IncidentDiffCallback()) {

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

class IncidentListener(val clickListener: (incidentId: String) -> Unit) {
    fun onClick(incident: Incident) = clickListener(incident.id)
}

