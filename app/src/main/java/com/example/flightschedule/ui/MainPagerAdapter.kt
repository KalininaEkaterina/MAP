package com.example.flightschedule.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flightschedule.ui.tabs.ArrivalsFragment
import com.example.flightschedule.ui.tabs.RouteFragment
import com.example.flightschedule.ui.tabs.TypesFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ArrivalsFragment()
        1 -> TypesFragment()
        else -> RouteFragment()
    }
}
