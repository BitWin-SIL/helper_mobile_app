package com.bitwin.helperapp.core.utilities

import com.bitwin.helperapp.BuildConfig

object Constant {
    const val SUPABASE_BASE_URL = "https://iidhoeaiqxvtydpqgnhy.supabase.co"
    const val SUPABASE_USER_DEVICES_ENDPOINT = "$SUPABASE_BASE_URL/rest/v1/user_devices"
    const val SUPABASE_API_KEY = BuildConfig.SUPABASE_API_KEY
}