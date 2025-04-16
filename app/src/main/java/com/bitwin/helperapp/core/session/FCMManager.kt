package com.bitwin.helperapp.core.session

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bitwin.helperapp.core.utilities.Constant
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

object FCMManager {
    private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

    fun sendDeviceTokenToSupabase(token: String, userId: Int = 1) {
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
