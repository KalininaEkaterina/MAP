package com.example.flightschedule.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightschedule.databinding.FragmentTypesBinding
import com.example.flightschedule.db.FlightRepository
import com.example.flightschedule.ui.AircraftTypeRowAdapter

class TypesFragment : Fragment() {

    private var _binding: FragmentTypesBinding? = null
    private val binding get() = _binding!!

    private val adapter = AircraftTypeRowAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTypesBinding.inflate(inflater, container, false)
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
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.buttonQuery.setOnClickListener {
            val code = airports.getOrNull(binding.spinnerAirport.selectedItemPosition)?.code
            if (code == null) {
                Toast.makeText(requireContext(), "Выберите аэропорт", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rows = repo.aircraftTypesForAirport(code)
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
