package com.bitwin.helperapp.features.association_requests.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class GetAssistanceRequestsResponse(
    val status: String,
    val message: String,
    val data: List<AssistanceRequestDto>
)

data class AssistanceRequestDto(
    val id: String,
    @SerializedName("requester_id") val requesterId: String,
    @SerializedName("respondent_id") val respondentId: String?,
    @SerializedName("request_type") val requestType: String,
    val status: String,
    val message: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("accepted_at") val acceptedAt: String?,
    @SerializedName("completed_at") val completedAt: String?
) {
    fun toDomainModel(): AssistanceRequest {
        return AssistanceRequest(
            id = id,
            requesterId = requesterId,
            respondentId = respondentId,
            requestType = requestType,
            status = status,
            message = message,
            createdAt = parseDate(createdAt),
            acceptedAt = acceptedAt?.let { parseDate(it) },
            completedAt = completedAt?.let { parseDate(it) }
        )
    }
    
    private fun parseDate(dateString: String): Date {
        return try {
            java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }
}

data class CreateAssistanceRequestResponse(
    @SerializedName("success") val success: Boolean,
    val data: AssistanceRequestDto?
)
