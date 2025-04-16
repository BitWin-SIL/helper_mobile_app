package com.bitwin.helperapp.core.session

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor(context: Context) {

    companion object {
        private const val PREF_NAME = "user_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val DEFAULT_USER_ID = ""
        private const val KEY_FCM_TOKEN_SENT = "fcm_token_sent"
    }

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _authState = MutableStateFlow(isLoggedIn())
    val authState: StateFlow<Boolean> = _authState.asStateFlow()

    fun isLoggedIn(): Boolean {
        return sharedPrefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserId(): String {
        return sharedPrefs.getString(KEY_USER_ID, DEFAULT_USER_ID) ?: DEFAULT_USER_ID
    }

    fun isFcmTokenSent(): Boolean {
        return sharedPrefs.getBoolean(KEY_FCM_TOKEN_SENT, false)
    }

    fun setFcmTokenSent(sent: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_FCM_TOKEN_SENT, sent).apply()
    }

    fun saveUserSession(userId: String) {
        sharedPrefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_FCM_TOKEN_SENT, false)
            apply()
        }
        _authState.value = true
    }

    fun clearSession() {
        sharedPrefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, false)
            putString(KEY_USER_ID, DEFAULT_USER_ID)
            apply()
        }
        _authState.value = false
    }
}