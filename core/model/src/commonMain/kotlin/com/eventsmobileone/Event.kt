package com.eventsmobileone

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val categoryId: String,
    val date: String, // ISO 8601 format: "2024-07-15T19:00:00Z"
    val location: String,
    val venue: String,
    val priceRange: PriceRange,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val organizer: String,
    val capacity: Int,
    val availableTickets: Int,
    val tags: List<String> = emptyList(),
    val isFeatured: Boolean = false
)

@Serializable
data class PriceRange(
    val minPrice: Double,
    val maxPrice: Double,
    val currency: String = "USD"
) {
    fun getDisplayText(): String {
        return if (minPrice == maxPrice) {
            "$currency $minPrice"
        } else {
            "$currency $minPrice - $maxPrice"
        }
    }
}

@Serializable
data class EventCategory(
    val id: String,
    val name: String,
    val icon: String, // Emoji or icon identifier
    val color: String? = null, // Hex color code
    val description: String = ""
)

@Serializable
data class EventFilter(
    val categoryId: String? = null,
    val dateRange: DateRange? = null,
    val priceRange: PriceRange? = null,
    val searchQuery: String = "",
    val sortBy: SortOption = SortOption.DATE_ASC
)

@Serializable
enum class SortOption {
    DATE_ASC,    // Upcoming first
    DATE_DESC,   // Latest first
    PRICE_ASC,   // Cheapest first
    PRICE_DESC,  // Most expensive first
    NAME_ASC,    // Alphabetical
    NAME_DESC    // Reverse alphabetical
}

@Serializable
data class DateRange(
    val startDate: String, // ISO 8601 format
    val endDate: String    // ISO 8601 format
)
