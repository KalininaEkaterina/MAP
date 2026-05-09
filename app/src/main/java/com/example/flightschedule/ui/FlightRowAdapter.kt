package com.example.flightschedule.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightschedule.data.FlightListItem
import com.example.flightschedule.databinding.ItemFlightRowBinding
import com.example.flightschedule.db.AircraftTypeIconMapper

class FlightRowAdapter : RecyclerView.Adapter<FlightRowAdapter.VH>() {

    var onReminderClick: ((FlightListItem) -> Unit)? = null

    private val items = ArrayList<FlightListItem>()

    fun submit(list: List<FlightListItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        val binding = ItemFlightRowBinding.inflate(inf, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], onReminderClick)
    }

    class VH(private val binding: ItemFlightRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: FlightListItem, onReminder: ((FlightListItem) -> Unit)?) {
            binding.iconType.setImageResource(AircraftTypeIconMapper.iconForTypeName(row.aircraftTypeName))
            binding.textFlightNumber.text = row.flightNumber
            binding.textTypeName.text = row.aircraftTypeName
            binding.textRoute.text = "${row.depCode} → ${row.arrCode}"
            binding.textDateTime.text = "${row.date}  ${row.depTime}"
            binding.buttonReminder.setOnClickListener { onReminder?.invoke(row) }
        }
    }
}
