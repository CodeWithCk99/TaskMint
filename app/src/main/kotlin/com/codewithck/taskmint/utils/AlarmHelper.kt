package com.codewithck.taskmint.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.codewithck.taskmint.NotificationReceiver

object AlarmHelper {

    fun scheduleReminder(
        context: Context,
        reminderTime: Long,
        title: String,
        message: String
    ) {

        if (reminderTime <= System.currentTimeMillis()) return

        val intent = Intent(
            context,
            NotificationReceiver::class.java
        ).apply {

            putExtra("title", title)
            putExtra("message", message)

        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderTime.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

    alarmManager.setAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        reminderTime,
        pendingIntent
    )

} else {

    alarmManager.set(
        AlarmManager.RTC_WAKEUP,
        reminderTime,
        pendingIntent
    )

}

    }

}