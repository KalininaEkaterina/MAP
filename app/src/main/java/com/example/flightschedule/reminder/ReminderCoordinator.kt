package com.example.flightschedule.reminder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.flightschedule.BuildConfig
import com.example.flightschedule.R
import com.example.flightschedule.data.FlightListItem
import com.example.flightschedule.ui.FlightRowAdapter

class ReminderCoordinator(private val fragment: Fragment) {

    private var pending: FlightListItem? = null

    private val permissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val ctx = fragment.context ?: return@registerForActivityResult
        if (granted) {
            pending?.let { applySchedule(ctx, it) }
        } else {
            Toast.makeText(ctx, R.string.reminder_permission_denied, Toast.LENGTH_SHORT).show()
        }
        pending = null
    }

    fun attach(adapter: FlightRowAdapter) {
        adapter.onReminderClick = { flight -> requestSchedule(flight) }
    }

    private fun requestSchedule(flight: FlightListItem) {
        val ctx = fragment.context ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    ctx,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> applySchedule(ctx, flight)
                else -> {
                    pending = flight
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            applySchedule(ctx, flight)
        }
    }

    private fun applySchedule(ctx: Context, flight: FlightListItem) {
        when (FlightReminderScheduler.schedule(ctx, flight)) {
            FlightReminderScheduler.ScheduleResult.Scheduled ->
                Toast.makeText(
                    ctx,
                    if (BuildConfig.DEBUG) R.string.reminder_scheduled_debug
                    else R.string.reminder_scheduled,
                    Toast.LENGTH_SHORT
                ).show()
            FlightReminderScheduler.ScheduleResult.TooLate ->
                Toast.makeText(ctx, R.string.reminder_too_late, Toast.LENGTH_SHORT).show()
            FlightReminderScheduler.ScheduleResult.InvalidTime ->
                Toast.makeText(ctx, R.string.reminder_invalid_time, Toast.LENGTH_SHORT).show()
        }
    }
}
