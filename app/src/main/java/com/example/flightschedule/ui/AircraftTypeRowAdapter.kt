package com.example.flightschedule.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightschedule.data.AircraftType
import com.example.flightschedule.databinding.ItemAircraftTypeRowBinding
import com.example.flightschedule.db.AircraftTypeIconMapper

class AircraftTypeRowAdapter : RecyclerView.Adapter<AircraftTypeRowAdapter.VH>() {

    private val items = ArrayList<AircraftType>()

    fun submit(list: List<AircraftType>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAircraftTypeRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(private val binding: ItemAircraftTypeRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(t: AircraftType) {
            binding.iconType.setImageResource(AircraftTypeIconMapper.iconForTypeName(t.name))
            binding.textName.text = t.name
            binding.textMeta.text =
                "Дальность: ${t.rangeKm} км · Эшелон: ${t.flightLevelM} м"
        }
    }
}
