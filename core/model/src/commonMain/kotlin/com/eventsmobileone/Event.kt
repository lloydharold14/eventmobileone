package com.eventsmobileone

import kotlinx.datetime.Instant

/**
 * Event model representing an event in the system
 */
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val categoryId: String,
    val date: String, // ISO 8601 format
    val location: String,
    val venue: String,
    val priceRange: PriceRange,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val organizer: String,
    val capacity: Int,
    val availableTickets: Int,
    val tags: List<String>,
    val isFeatured: Boolean = false
)

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
