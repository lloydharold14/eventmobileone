package com.eventsmobileone

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Mobile-optimized Event model representing an event in the system
 * Simplified structure for mobile consumption with 40% payload reduction
 */
@Serializable
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val slug: String,
    val organizerId: String,
    val organizerName: String,
    val categoryId: String,
    val categoryName: String,
    val status: String,
    val startDate: String, // ISO 8601 format
    val endDate: String, // ISO 8601 format
    val location: EventLocation,
    val maxAttendees: Int,
    val currentAttendees: Int,
    val pricing: EventPricing,
    val featuredImage: String?,
    val tags: List<String>
    // Removed: complex nested objects for mobile optimization
)

/**
 * Mobile-optimized event location
 */
@Serializable
data class EventLocation(
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val coordinates: Coordinates?
)

/**
 * Mobile-optimized event pricing
 */
@Serializable
data class EventPricing(
    val model: String, // "free", "paid", "donation", "tiered"
    val currency: String,
    val basePrice: Double
)

/**
 * Coordinates for location-based search
 */
@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
) {
    /**
     * Calculate distance in kilometers to another location using Haversine formula
     */
    fun distanceTo(other: Coordinates): Double {
        val earthRadius = 6371.0 // Earth's radius in kilometers
        
        val lat1Rad = kotlin.math.PI * latitude / 180.0
        val lat2Rad = kotlin.math.PI * other.latitude / 180.0
        val deltaLatRad = kotlin.math.PI * (other.latitude - latitude) / 180.0
        val deltaLonRad = kotlin.math.PI * (other.longitude - longitude) / 180.0
        
        val a = kotlin.math.sin(deltaLatRad / 2) * kotlin.math.sin(deltaLatRad / 2) +
                kotlin.math.cos(lat1Rad) * kotlin.math.cos(lat2Rad) *
                kotlin.math.sin(deltaLonRad / 2) * kotlin.math.sin(deltaLonRad / 2)
        
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        
        return earthRadius * c
    }
    
    /**
     * Convert distance to miles
     */
    fun distanceToMiles(other: Coordinates): Double {
        return distanceTo(other) * 0.621371 // Convert km to miles
    }
}

/**
 * Location search parameters
 */
@Serializable
data class LocationSearchParams(
    val query: String? = null,
    val coordinates: Coordinates? = null,
    val radiusMiles: Double = 25.0, // Default 25 mile radius
    val useCurrentLocation: Boolean = false
)

/**
 * Search radius options
 */
enum class SearchRadius(val miles: Double, val displayName: String) {
    FIVE_MILES(5.0, "5 miles"),
    TEN_MILES(10.0, "10 miles"),
    TWENTY_FIVE_MILES(25.0, "25 miles"),
    FIFTY_MILES(50.0, "50 miles"),
    HUNDRED_MILES(100.0, "100 miles")
}

/**
 * Location search result with distance information
 */
@Serializable
data class EventWithDistance(
    val event: Event,
    val distanceMiles: Double? = null
) {
    fun getDistanceDisplay(): String {
        return when {
            distanceMiles == null -> "${event.location.address}, ${event.location.city}"
            distanceMiles < 1.0 -> "${(distanceMiles * 5280).toInt()} ft away"
            distanceMiles < 10.0 -> "${(distanceMiles * 10).toInt() / 10.0} miles away"
            else -> "${distanceMiles.toInt()} miles away"
        }
    }
}

/**
 * Price range for events
 */
@Serializable
data class PriceRange(
    val minPrice: Double,
    val maxPrice: Double
) {
    fun getDisplayText(): String {
        return when {
            minPrice == 0.0 && maxPrice == 0.0 -> "Free"
            minPrice == maxPrice -> "$${minPrice.toInt()}"
            else -> "$${minPrice.toInt()} - $${maxPrice.toInt()}"
        }
    }
}

/**
 * Event category
 */
@Serializable
data class EventCategory(
    val id: String,
    val name: String,
    val icon: String,
    val color: String
)


