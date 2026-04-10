package com.didiermendoza.tandamex.src.core.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.didiermendoza.tandamex.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationFactory @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun createSyncNotification(contentText: String): Notification {
        return NotificationCompat.Builder(context, "sync_channel")
            .setContentTitle("Sincronizando Perfil")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}