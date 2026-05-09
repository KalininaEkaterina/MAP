package com.example.flightschedule.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightschedule.databinding.FragmentArrivalsBinding
import com.example.flightschedule.db.FlightRepository
import com.example.flightschedule.ui.FlightRowAdapter

class ArrivalsFragment : Fragment() {

    private var _binding: FragmentArrivalsBinding? = null
    private val binding get() = _binding!!

    private val adapter = FlightRowAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArrivalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = FlightRepository(requireContext())
        val airports = repo.allAirports()
        val labels = airports.map { "${it.code} — ${it.name}" }
        binding.spinnerAirport.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            labels
        )
        if (binding.editDate.text.isNullOrBlank()) {
            binding.editDate.setText("2026-05-05")
        }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.buttonQuery.setOnClickListener {
            val code = airports.getOrNull(binding.spinnerAirport.selectedItemPosition)?.code
            val date = binding.editDate.text?.toString()?.trim().orEmpty()
            if (code == null || date.isEmpty()) {
                Toast.makeText(requireContext(), "Выберите аэропорт и дату", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val rows = repo.arrivalsAtAirport(code, date)
            adapter.submit(rows)
            if (rows.isEmpty()) {
                Toast.makeText(requireContext(), "Нет данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
