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
    val createdAt: Date,
    val acceptedAt: Date?,
    val completedAt: Date?
) {
    fun toUiModel(): com.bitwin.helperapp.features.association_requests.ui.AssistanceRequest {
        val requestStatus = when (status) {
            "PENDING" -> com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus.Pending
            "ACCEPTED" -> com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus.Accepted
            "REJECTED" -> com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus.Rejected
            else -> com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus.Pending
        }
        
        return com.bitwin.helperapp.features.association_requests.ui.AssistanceRequest(
            id = id,
            email = respondentId ?: "",
            date = createdAt,
            status = requestStatus
        )
    }
}
