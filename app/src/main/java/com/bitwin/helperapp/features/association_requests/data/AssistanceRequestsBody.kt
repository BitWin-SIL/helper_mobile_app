package com.bitwin.helperapp.features.association_requests.data

import com.google.gson.annotations.SerializedName

data class CreateAssistanceRequestBody(
    @SerializedName("visually_impaired_email") val visuallyImpairedEmail: String,
    @SerializedName("visually_impaired_id") val visuallyImpairedId: String,
    @SerializedName("visually_impaired_name") val visuallyImpairedName: String? = null,
    val message: String? = "I would like to be your helper."
)
