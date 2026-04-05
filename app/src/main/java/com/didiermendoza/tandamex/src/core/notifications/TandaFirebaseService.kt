package com.didiermendoza.tandamex.src.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class TandaFirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.data["title"] ?: "¡Notificación de Tandamex!"
        val body = message.data["body"] ?: "Tienes una nueva actualización en tu tanda."
        val tandaId = message.data["tandaId"]
        showVisualNotification(title, body)
    }

    private fun showVisualNotification(title: String, body: String) {
        val channelId = "tanda_payments_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // En Android 8.0 o superior, Google te obliga a crear un "Canal" de notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Actualizaciones de Tandas", // Esto es lo que el usuario ve en los ajustes de su celular
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Construimos la tarjetita que sale arriba
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Cambia esto por el logo de tu app después
            .setAutoCancel(true) // Hace que desaparezca cuando la tocas
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Le damos un ID al azar para que si llegan 3 pagos, salgan 3 notificaciones y no se sobreescriban
        notificationManager.notify(Random.nextInt(), notification)
    }

    // Por si Google decide cambiar el token de seguridad mientras la app está cerrada
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("🔥 FCM Token actualizado por Google: $token")
        // La próxima vez que abra la app, tu HomeViewModel lo guardará en la BD
    }
}