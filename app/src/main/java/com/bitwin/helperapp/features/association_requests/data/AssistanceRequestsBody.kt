package com.bitwin.helperapp.features.association_requests.data

import com.google.gson.annotations.SerializedName

data class CreateAssistanceRequestBody(
    @SerializedName("respondent_id") val respondentEmail: String,
    @SerializedName("request_type") val requestType: String = "ASSISTANCE",
    val message: String? = "Association request"
)
