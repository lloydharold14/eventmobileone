package com.eventsmobileone

import kotlinx.coroutines.flow.Flow

/**
 * Platform-agnostic location service interface
 */
interface LocationService {
    /**
     * Get current location if permission is granted
     */
    suspend fun getCurrentLocation(): Coordinates?
    
    /**
     * Request location permission
     * @return true if permission was granted, false otherwise
     */
    suspend fun requestLocationPermission(): Boolean
    
    /**
     * Check if location permission is granted
     */
    suspend fun hasLocationPermission(): Boolean
    
    /**
     * Observe location updates
     */
    fun observeLocationUpdates(): Flow<Coordinates>
    
    /**
     * Geocode a location string to coordinates
     */
    suspend fun geocodeLocation(location: String): Coordinates?
    
    /**
     * Reverse geocode coordinates to location string
     */
    suspend fun reverseGeocode(coordinates: Coordinates): String?
}

/**
 * Location permission status
 */
enum class LocationPermissionStatus {
    GRANTED,
    DENIED,
    NOT_REQUESTED
}

/**
 * Location service result
 */
sealed class LocationResult {
    data class Success(val coordinates: Coordinates) : LocationResult()
    data class Error(val message: String) : LocationResult()
    data object PermissionDenied : LocationResult()
    data object LocationUnavailable : LocationResult()
}
