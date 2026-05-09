package com.example.flightschedule.db

import androidx.annotation.DrawableRes
import com.example.flightschedule.R

object AircraftTypeIconMapper {

    @DrawableRes
    fun iconForTypeName(typeName: String): Int {
        val n = typeName.lowercase()
        return when {
            n.contains("777") || n.contains("wide") || n.contains("350") -> R.drawable.ic_type_wide
            n.contains("atr") || n.contains("turboprop") || n.contains("dash") ->
                R.drawable.ic_type_turboprop
            n.contains("a320") || n.contains("a319") || n.contains("a321") ||
                n.contains("737") || n.contains("embraer") || n.contains("e1") ->
                R.drawable.ic_type_narrow
            else -> R.drawable.ic_type_regional
        }
    }
}
