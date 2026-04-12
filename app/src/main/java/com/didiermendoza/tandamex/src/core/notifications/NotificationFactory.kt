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
        val channelId = "profile_sync_channel"

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Sincronizando Perfil")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}