package com.eventsmobileone

import kotlinx.datetime.Instant

/**
 * Event model representing an event in the system
 * Updated to match the real API structure with enhanced localization
 */
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val localizedContent: Map<String, LocalizedContent>? = null,
    val startDate: String, // ISO 8601 format
    val endDate: String, // ISO 8601 format
    val location: EventLocation,
    val organizer: EventOrganizer,
    val category: String,
    val pricing: EventPricing,
    val images: List<String>,
    val status: String,
    val maxAttendees: Int,
    val currentAttendees: Int,
    val tags: List<String>,
    val features: List<String>,
    val compliance: EventCompliance? = null,
    // Mobile-specific fields for attendee experience
    val isBookmarked: Boolean = false,
    val isNearby: Boolean = false,
    val distance: Double? = null,
    val localContent: MobileEventLocalContent? = null
)

/**
 * Localized content for different languages
 */
data class LocalizedContent(
    val title: String,
    val description: String
)

/**
 * Mobile-optimized event content
 */
data class MobileEventLocalContent(
    val title: String,
    val description: String,
    val currency: String,
    val formattedPrice: String
)

/**
 * Event location with detailed information
 */
data class EventLocation(
    val address: String,
    val city: String,
    val province: String,
    val country: String,
    val coordinates: Coordinates?,
    val venue: Venue? = null
)

/**
 * Venue information
 */
data class Venue(
    val name: String,
    val description: String? = null,
    val capacity: Int? = null,
    val amenities: List<String>? = null
)

/**
 * Event organizer information
 */
data class EventOrganizer(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val website: String? = null,
    val logo: String? = null
)

/**
 * Event pricing with multi-currency support
 */
data class EventPricing(
    val baseCurrency: String,
    val baseAmount: Double,
    val displayCurrency: String,
    val displayAmount: Double,
    val exchangeRate: Double,
    val availableCurrencies: Map<String, CurrencyInfo>,
    val tiers: List<PricingTier>? = null
)

/**
 * Pricing tier for different ticket types
 */
data class PricingTier(
    val id: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    val currency: String,
    val availableQuantity: Int,
    val maxQuantityPerPurchase: Int? = null,
    val benefits: List<String>? = null
)

/**
 * Currency information
 */
data class CurrencyInfo(
    val amount: Double,
    val formatted: String
)

/**
 * Event compliance information
 */
data class EventCompliance(
    val taxInformation: Map<String, TaxInfo>? = null
)

/**
 * Tax information
 */
data class TaxInfo(
    val taxRate: Double,
    val taxType: String,
    val taxAmount: Double,
    val currency: String
)

/**
 * Coordinates for location-based search
 */
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
data class EventCategory(
    val id: String,
    val name: String,
    val icon: String,
    val color: String
)


