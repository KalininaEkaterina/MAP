package com.example.flightschedule.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.flightschedule.R

object FlightNotifications {

    const val CHANNEL_ID = "flight_reminders"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val mgr = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notif_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notif_channel_desc)
        }
        mgr.createNotificationChannel(channel)
    }

    fun showReminder(
        context: Context,
        notificationId: Int,
        flightNumber: String,
        details: String,
        debug15sDemo: Boolean = false
    ) {
        ensureChannel(context)
        val mgr = NotificationManagerCompat.from(context)
        val titleRes = if (debug15sDemo) R.string.notif_title_debug else R.string.notif_title
        val bigText = if (debug15sDemo) {
            details + context.getString(R.string.notif_text_debug_suffix)
        } else {
            details
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_plane)
            .setContentTitle(context.getString(titleRes, flightNumber))
            .setContentText(bigText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        mgr.notify(notificationId, notification)
    }
}
