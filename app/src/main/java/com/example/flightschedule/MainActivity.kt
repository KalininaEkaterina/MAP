package com.example.flightschedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flightschedule.databinding.ActivityMainBinding
import com.example.flightschedule.ui.MainPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.pager.adapter = MainPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.pager) { tab, pos ->
            tab.text = when (pos) {
                0 -> getString(R.string.tab_arrivals)
                1 -> getString(R.string.tab_types)
                else -> getString(R.string.tab_route)
            }
        }.attach()
    }
}
