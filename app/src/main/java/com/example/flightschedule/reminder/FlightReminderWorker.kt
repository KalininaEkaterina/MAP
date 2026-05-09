package com.example.flightschedule.reminder

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class FlightReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val number = inputData.getString(KEY_FLIGHT_NUMBER).orEmpty()
        val details = inputData.getString(KEY_DETAILS).orEmpty()
        if (number.isEmpty()) return Result.failure()

        val id = inputData.getInt(KEY_NOTIFICATION_ID, number.hashCode())
        val debug15s = inputData.getBoolean(KEY_DEBUG_15S, false)
        FlightNotifications.showReminder(
            applicationContext,
            id,
            number,
            details,
            debug15s
        )
        return Result.success()
    }

    companion object {
        const val KEY_FLIGHT_NUMBER = "flight_number"
        const val KEY_DETAILS = "details"
        const val KEY_NOTIFICATION_ID = "notification_id"
        const val KEY_DEBUG_15S = "debug_15s"

        fun inputFor(
            flightNumber: String,
            details: String,
            notificationId: Int,
            debug15s: Boolean = false
        ) = workDataOf(
            KEY_FLIGHT_NUMBER to flightNumber,
            KEY_DETAILS to details,
            KEY_NOTIFICATION_ID to notificationId,
            KEY_DEBUG_15S to debug15s
        )
    }
}
