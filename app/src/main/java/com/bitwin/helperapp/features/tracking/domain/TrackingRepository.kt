package com.bitwin.helperapp.features.tracking.domain

import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.tracking.data.TrackingRequest
import com.bitwin.helperapp.features.tracking.data.TrackingResponse
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.delay
import javax.inject.Inject

@ActivityScoped
class TrackingRepository @Inject constructor() {
    suspend fun track(request: TrackingRequest): Resource<TrackingResponse> {
        delay(1000) // simulate network delay
        val response = TrackingResponse(
            currentLatitude = request.latitude + 0.001,
            currentLongitude = request.longitude + 0.001,
            distanceTraveled = 1.23
        )
        return Resource.Success(response)
    }
}
