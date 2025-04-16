package com.bitwin.helperapp.core.services

import android.os.Build
import androidx.annotation.RequiresApi
import com.bitwin.helperapp.R
import com.bitwin.helperapp.core.session.UserSessionManager
import com.bitwin.helperapp.core.utilities.Constant
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                sendDeviceTokenToSupabase(token)
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
    
        remoteMessage.notification?.let {
            val title = it.title ?: "New Notification"
            val body = it.body ?: ""
            showNotification(title, body)
        }
    
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

    private fun sendDeviceTokenToSupabase(token: String) {
        val userId = UserSessionManager.getUserId()
        val client = OkHttpClient()
        val json = """
            {
                "user_id": $userId,
                "device_token": "$token",
                "platform": "android",
                "is_active": true
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(Constant.SUPABASE_USER_DEVICES_ENDPOINT)
            .addHeader("apikey", Constant.SUPABASE_API_KEY)
            .addHeader("Authorization", "Bearer ${Constant.SUPABASE_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .addHeader("Prefer", "return=representation")
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Failed to send device token: ${response.code}")
            }
        }
    }
    
}