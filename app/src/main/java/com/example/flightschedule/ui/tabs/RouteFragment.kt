package com.example.flightschedule.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightschedule.databinding.FragmentRouteBinding
import com.example.flightschedule.db.FlightRepository
import com.example.flightschedule.ui.FlightRowAdapter

class RouteFragment : Fragment() {

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private val adapter = FlightRowAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = FlightRepository(requireContext())
        val airports = repo.allAirports()
        val labels = airports.map { "${it.code} — ${it.name}" }
        fun makeAdapter() = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            labels
        )
        binding.spinnerDep.adapter = makeAdapter()
        binding.spinnerArr.adapter = makeAdapter()
        if (binding.editDate.text.isNullOrBlank()) {
            binding.editDate.setText("2026-05-05")
        }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.buttonQuery.setOnClickListener {
            val dep = airports.getOrNull(binding.spinnerDep.selectedItemPosition)?.code
            val arr = airports.getOrNull(binding.spinnerArr.selectedItemPosition)?.code
            val date = binding.editDate.text?.toString()?.trim().orEmpty()
            if (dep == null || arr == null || date.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dep == arr) {
                Toast.makeText(requireContext(), "Пункты должны различаться", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val rows = repo.flightsByRoute(dep, arr, date)
            adapter.submit(rows)
            if (rows.isEmpty()) {
                Toast.makeText(requireContext(), "Нет рейсов", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
