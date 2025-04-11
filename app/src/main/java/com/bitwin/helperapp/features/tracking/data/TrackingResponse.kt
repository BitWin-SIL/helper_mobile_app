package com.bitwin.helperapp.features.tracking.data

data class TrackingResponse(
    val currentLatitude: Double,
    val currentLongitude: Double,
    val distanceTraveled: Double
)