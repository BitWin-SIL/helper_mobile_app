package com.bitwin.helperapp.features.association_requests.data

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

// GetAssistanceRequestsResponse class is no longer needed as we're getting a direct array

data class AssistanceRequestDto(
    val id: String,
    @SerializedName("requester_id") val requesterId: Any, // Handle both String and Number
    @SerializedName("respondent_id") val respondentId: Any?, // Handle both String and Number
    @SerializedName("request_type") val requestType: String,
    val status: String,
    val message: String?,
    @SerializedName("location_id") val locationId: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("accepted_at") val acceptedAt: String?,
    @SerializedName("completed_at") val completedAt: String?
) {
    fun toDomainModel(): AssistanceRequest {
        return AssistanceRequest(
            id = id,
            requesterId = requesterId.toString(),
            respondentId = respondentId?.toString(),
            requestType = requestType,
            status = status,
            message = message,
            locationId = locationId,
            createdAt = parseDate(createdAt),
            acceptedAt = acceptedAt?.let { parseDate(it) },
            completedAt = completedAt?.let { parseDate(it) }
        )
    }
    
    private fun parseDate(dateString: String): Date {
        return try {
            // Try multiple date formats to handle different API response formats
            val formats = arrayOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'+'HH:mm"
            )
            
            for (format in formats) {
                try {
                    return SimpleDateFormat(format, Locale.getDefault())
                        .apply { timeZone = TimeZone.getTimeZone("UTC") }
                        .parse(dateString) ?: Date()
                } catch (e: Exception) {
                    // Try next format
                }
            }
            Date()
        } catch (e: Exception) {
            Date()
        }
    }
}

data class CreateAssistanceRequestResponse(
    @SerializedName("success") val success: Boolean,
    val data: AssistanceRequestDto?
)
