package com.bitwin.helperapp.features.profile.data

data class UserInfoResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String? = null,
)
