package com.bitwin.helperapp.features.register.data

data class RegisterRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val role: String = "helper"
)