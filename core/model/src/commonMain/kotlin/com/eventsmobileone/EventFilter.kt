package com.eventsmobileone

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/**
 * Enhanced Event Filter for eventMO-1002
 * Supports filtering by date, price, availability, and other criteria
 */
data class EventFilter(
    val categoryId: String? = null,
    val searchQuery: String = "",
    val sortBy: SortOption = SortOption.DATE_ASC,
    
    // Date filtering
    val dateRange: DateRange = DateRange.ALL,
    val customStartDate: LocalDate? = null,
    val customEndDate: LocalDate? = null,
    
    // Price filtering
    val priceRange: PriceRangeFilter = PriceRangeFilter.ALL,
    val customMinPrice: Double? = null,
    val customMaxPrice: Double? = null,
    
    // Availability filtering
    val availability: AvailabilityFilter = AvailabilityFilter.ALL,
    
    // Location filtering (for future use)
    val location: String? = null,
    val radius: Int? = null
)

/**
 * Date range options for filtering
 */
enum class DateRange(val displayName: String) {
    ALL("All Dates"),
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month"),
    NEXT_MONTH("Next Month"),
    CUSTOM("Custom Range")
}

/**
 * Price range filter options
 */
enum class PriceRangeFilter(val displayName: String, val minPrice: Double?, val maxPrice: Double?) {
    ALL("All Prices", null, null),
    FREE("Free", 0.0, 0.0),
    UNDER_50("Under $50", 0.0, 49.99),
    FIFTY_TO_100("$50 - $100", 50.0, 100.0),
    OVER_100("Over $100", 100.01, null),
    CUSTOM("Custom Range", null, null)
}

/**
 * Availability filter options
 */
enum class AvailabilityFilter(val displayName: String) {
    ALL("All Events"),
    AVAILABLE("Available"),
    LIMITED("Limited"),
    SOLD_OUT("Sold Out")
}

/**
 * Sort options for events
 */
enum class SortOption(val displayName: String) {
    DATE_ASC("Date (Earliest First)"),
    DATE_DESC("Date (Latest First)"),
    PRICE_ASC("Price (Low to High)"),
    PRICE_DESC("Price (High to Low)"),
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    POPULARITY("Most Popular")
}
