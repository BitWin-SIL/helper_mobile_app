package com.bitwin.helperapp.features.register.data

import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}