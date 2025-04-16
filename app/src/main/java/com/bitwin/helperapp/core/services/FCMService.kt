package com.bitwin.helperapp.core.services

import android.os.Build
import androidx.annotation.RequiresApi
import com.bitwin.helperapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.bitwin.helperapp.core.session.FCMManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FCMManager.sendDeviceTokenToSupabase(token, 1)
                Timber.d("FCMService: FCM token sent successfully")
            } catch (e: Exception) {
                Timber.e(e, "FCMService: Error sending FCM token")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    
        Timber.d("FCMService: Message received: $remoteMessage")
    
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            val title = it.title ?: "New Notification"
            val body = it.body ?: ""
            showNotification(title, body)
        }
    
        // Optional: Handle data payload if needed
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("FCMService: Data payload: ${remoteMessage.data}")
            val title = remoteMessage.data["title"] ?: "New Notification"
            val body = remoteMessage.data["body"] ?: remoteMessage.data["message"] ?: ""
            showNotification(title, body)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel_id"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
    
        // For Android 8+, create notification channel
        val channel = android.app.NotificationChannel(
            channelId,
            "Default Channel",
            android.app.NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val builder = android.app.Notification.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
    
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }    
    
}