package com.bitwin.helperapp.core.api

import com.bitwin.helperapp.features.register.data.RegisterRequest
import com.bitwin.helperapp.features.register.data.RegisterResponse
import com.bitwin.helperapp.features.login.data.LoginRequest
import com.bitwin.helperapp.features.login.data.LoginResponse
import com.bitwin.helperapp.features.profile.data.UserInfoRequest
import com.bitwin.helperapp.features.profile.data.UserInfoResponse
import com.bitwin.helperapp.features.association_requests.data.CreateAssistanceRequestBody
import com.bitwin.helperapp.features.association_requests.data.CreateAssistanceRequestResponse
import com.bitwin.helperapp.features.association_requests.data.GetAssistanceRequestsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HelperApi {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("user/info")
    suspend fun getUserInfo(@Body request: UserInfoRequest): UserInfoResponse
    
    @GET("assistance/requests")
    suspend fun getAssistanceRequests(): GetAssistanceRequestsResponse
    
    @POST("/functions/v1/assistance-requests/request-to-assist")
    suspend fun createAssistanceRequest(@Body request: CreateAssistanceRequestBody): CreateAssistanceRequestResponse
}
