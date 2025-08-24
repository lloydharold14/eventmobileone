package com.eventsmobileone.repository

import com.eventsmobileone.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Mock implementation of LocationService for testing
 */
class MockLocationService : LocationService {
    
    private var hasPermission = false
    private val mockCurrentLocation = Coordinates(40.7128, -74.0060) // New York City
    
    override suspend fun getCurrentLocation(): Coordinates? {
        return if (hasPermission) mockCurrentLocation else null
    }
    
    override suspend fun requestLocationPermission(): Boolean {
        hasPermission = true
        return true
    }
    
    override suspend fun hasLocationPermission(): Boolean {
        return hasPermission
    }
    
    override fun observeLocationUpdates(): Flow<Coordinates> {
        return flow {
            if (hasPermission) {
                emit(mockCurrentLocation)
            }
        }
    }
    
    override suspend fun geocodeLocation(location: String): Coordinates? {
        // Mock geocoding - return coordinates for common cities
        return when (location.lowercase()) {
            "new york", "nyc", "new york city" -> Coordinates(40.7128, -74.0060)
            "los angeles", "la" -> Coordinates(34.0522, -118.2437)
            "chicago" -> Coordinates(41.8781, -87.6298)
            "miami" -> Coordinates(25.7617, -80.1918)
            "san francisco", "sf" -> Coordinates(37.7749, -122.4194)
            "boston" -> Coordinates(42.3601, -71.0589)
            "seattle" -> Coordinates(47.6062, -122.3321)
            "denver" -> Coordinates(39.7392, -104.9903)
            "atlanta" -> Coordinates(33.7490, -84.3880)
            "dallas" -> Coordinates(32.7767, -96.7970)
            else -> null
        }
    }
    
    override suspend fun reverseGeocode(coordinates: Coordinates): String? {
        // Mock reverse geocoding
        return when {
            coordinates.latitude == 40.7128 && coordinates.longitude == -74.0060 -> "New York, NY"
            coordinates.latitude == 34.0522 && coordinates.longitude == -118.2437 -> "Los Angeles, CA"
            coordinates.latitude == 41.8781 && coordinates.longitude == -87.6298 -> "Chicago, IL"
            coordinates.latitude == 25.7617 && coordinates.longitude == -80.1918 -> "Miami, FL"
            coordinates.latitude == 37.7749 && coordinates.longitude == -122.4194 -> "San Francisco, CA"
            else -> "Unknown Location"
        }
    }
}
