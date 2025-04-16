package com.bitwin.helperapp.core.api

import com.bitwin.helperapp.features.register.data.RegisterRequest
import com.bitwin.helperapp.features.register.data.RegisterResponse
import com.bitwin.helperapp.features.login.data.LoginRequest
import com.bitwin.helperapp.features.login.data.LoginResponse
import com.bitwin.helperapp.features.profile.data.UserInfoRequest
import com.bitwin.helperapp.features.profile.data.UserInfoResponse
import com.bitwin.helperapp.features.association_requests.data.AssistanceRequestDto
import com.bitwin.helperapp.features.association_requests.data.CreateAssistanceRequestBody
import com.bitwin.helperapp.features.association_requests.data.CreateAssistanceRequestResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HelperApi {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("user/info")
    suspend fun getUserInfo(@Body request: UserInfoRequest): UserInfoResponse
    
    @GET("/rest/v1/assistance_requests")
    suspend fun getAssistanceRequests(
        @Query("select") select: String = "id,requester_id,respondent_id,request_type,status,message,location_id,created_at,accepted_at,completed_at",
        @Query("requester_id") requesterId: String
    ): List<AssistanceRequestDto>
    
    @POST("/functions/v1/assistance-requests/request-to-assist")
    suspend fun createAssistanceRequest(@Body request: CreateAssistanceRequestBody): CreateAssistanceRequestResponse
}
