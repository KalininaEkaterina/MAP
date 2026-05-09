package com.example.flightschedule

import android.app.Application
import com.example.flightschedule.reminder.FlightNotifications

class FlightScheduleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FlightNotifications.ensureChannel(this)
    }
}
