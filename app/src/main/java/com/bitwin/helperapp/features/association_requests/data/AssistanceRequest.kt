package com.bitwin.helperapp.features.association_requests.data

import androidx.compose.runtime.Immutable
import java.util.*

@Immutable
data class AssistanceRequest(
    val id: String,
    val requesterId: String,
    val respondentId: String?,
    val requestType: String,
    val status: String,
    val message: String?,
    val locationId: String?, // new property to match 'location_id'
    val createdAt: Date,
    val acceptedAt: Date?,
    val completedAt: Date?
) {
    fun toUiModel(): com.bitwin.helperapp.features.association_requests.ui.AssistanceRequest {
        val requestStatus = when (status.lowercase()) {
            "accepted" -> com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus.Accepted
            "rejected" -> com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus.Rejected
            else -> com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus.Pending
        }
        
        return com.bitwin.helperapp.features.association_requests.ui.AssistanceRequest(
            id = id,
            email = respondentId ?: requesterId, // Use requesterId as fallback instead of empty string
            date = createdAt,
            status = requestStatus
        )
    }
}
