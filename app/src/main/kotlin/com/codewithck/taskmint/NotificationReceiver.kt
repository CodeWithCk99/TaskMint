package com.codewithck.taskmint

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        NotificationHelper.showNotification(
            context,
            intent.getStringExtra("title") ?: "Task Reminder",
            intent.getStringExtra("message") ?: "You have a pending task."
        )

    }
}