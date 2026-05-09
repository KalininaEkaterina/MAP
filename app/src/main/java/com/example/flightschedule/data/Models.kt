package com.example.flightschedule.data

data class Airport(
    val code: String,
    val name: String
)

data class AircraftType(
    val id: Long,
    val name: String,
    val rangeKm: Int,
    val flightLevelM: Int
)

data class FlightListItem(
    val id: Long,
    val flightNumber: String,
    val aircraftTypeName: String,
    val depCode: String,
    val arrCode: String,
    val date: String,
    val depTime: String
)
