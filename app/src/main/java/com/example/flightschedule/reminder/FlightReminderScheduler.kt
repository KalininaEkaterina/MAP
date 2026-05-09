package com.example.flightschedule.reminder

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.flightschedule.BuildConfig
import com.example.flightschedule.data.FlightListItem
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object FlightReminderScheduler {

    enum class ScheduleResult {
        Scheduled,
        TooLate,
        InvalidTime
    }

    fun schedule(context: Context, flight: FlightListItem): ScheduleResult {
        if (BuildConfig.DEBUG) {
            return scheduleDebug15Seconds(context, flight)
        }
        val zone = ZoneId.systemDefault()
        val departure = parseDeparture(flight, zone) ?: return ScheduleResult.InvalidTime
        val remindAt = departure.minusHours(3)
        val now = ZonedDateTime.now(zone)
        if (!remindAt.isAfter(now)) return ScheduleResult.TooLate

        val delay = Duration.between(now.toInstant(), remindAt.toInstant())
        val details =
            "${flight.depCode} → ${flight.arrCode}, вылет ${flight.date} ${flight.depTime}"

        val notificationId = (31 * flight.id + flight.flightNumber.hashCode()).toInt()
        val request = OneTimeWorkRequestBuilder<FlightReminderWorker>()
            .setInitialDelay(delay)
            .setInputData(
                FlightReminderWorker.inputFor(
                    flight.flightNumber,
                    details,
                    notificationId
                )
            )
            .build()

        WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
            uniqueWorkName(flight),
            ExistingWorkPolicy.REPLACE,
            request
        )
        return ScheduleResult.Scheduled
    }

    private fun scheduleDebug15Seconds(context: Context, flight: FlightListItem): ScheduleResult {
        val delay = Duration.ofSeconds(15)
        val details =
            "${flight.depCode} → ${flight.arrCode}, вылет ${flight.date} ${flight.depTime}"
        val notificationId = (31 * flight.id + flight.flightNumber.hashCode()).toInt()
        val request = OneTimeWorkRequestBuilder<FlightReminderWorker>()
            .setInitialDelay(delay)
            .setInputData(
                FlightReminderWorker.inputFor(
                    flight.flightNumber,
                    details,
                    notificationId,
                    debug15s = true
                )
            )
            .build()
        WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
            uniqueWorkName(flight),
            ExistingWorkPolicy.REPLACE,
            request
        )
        return ScheduleResult.Scheduled
    }

    private fun uniqueWorkName(f: FlightListItem) = "flight_reminder_${f.id}"

    private fun parseDeparture(flight: FlightListItem, zone: ZoneId): ZonedDateTime? {
        return try {
            val d = LocalDate.parse(flight.date)
            val t = LocalTime.parse(flight.depTime)
            ZonedDateTime.of(d, t, zone)
        } catch (_: Exception) {
            null
        }
    }
}
