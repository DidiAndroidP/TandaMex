package com.didiermendoza.tandamex.src.core.services

import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.didiermendoza.tandamex.src.core.notifications.NotificationFactory
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.UpdateProfileUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.UploadProfilePhotoUseCase

@AndroidEntryPoint
class DataSyncService : Service() {
    @Inject lateinit var uploadProfilePhotoUseCase: UploadProfilePhotoUseCase
    @Inject lateinit var updateProfileUseCase: UpdateProfileUseCase
    @Inject lateinit var notificationFactory: NotificationFactory
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        private const val NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "profile_sync_channel"
        const val ACTION_UPLOAD_PHOTO = "ACTION_UPLOAD_PHOTO"
        const val ACTION_UPDATE_PROFILE = "ACTION_UPDATE_PROFILE"
        const val EXTRA_FILE_PATH = "extra_file_path"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHONE = "extra_phone"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        createNotificationChannel()

        val message = when (action) {
            ACTION_UPDATE_PROFILE -> "Actualizando datos del usuario..."
            ACTION_UPLOAD_PHOTO -> "Subiendo foto de perfil..."
            else -> "Procesando datos..."
        }

        val notification = notificationFactory.createSyncNotification(message)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            }
            else -> {
                startForeground(NOTIFICATION_ID, notification)
            }
        }

        when (action) {
            ACTION_UPDATE_PROFILE -> {
                val name = intent?.getStringExtra(EXTRA_NAME) ?: ""
                val phone = intent?.getStringExtra(EXTRA_PHONE) ?: ""
                updateProfileTask(name, phone)
            }
            ACTION_UPLOAD_PHOTO -> {
                val filePath = intent?.getStringExtra(EXTRA_FILE_PATH) ?: ""
                uploadPhotoTask(filePath)
            }
        }

        return START_NOT_STICKY
    }

    private fun updateProfileTask(name: String, phone: String) {
        serviceScope.launch {
            updateProfileUseCase(name, phone)
            stopServiceGracefully()
        }
    }

    private fun uploadPhotoTask(filePath: String) {
        serviceScope.launch {
            uploadProfilePhotoUseCase(filePath)
            stopServiceGracefully()
        }
    }

    private fun stopServiceGracefully() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Sincronización de Perfil",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}