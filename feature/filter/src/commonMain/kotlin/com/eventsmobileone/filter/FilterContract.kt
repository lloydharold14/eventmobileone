package com.eventsmobileone.filter

import com.eventsmobileone.*

/**
 * UI State for Filter Screen
 */
data class FilterUiState(
    val dateRange: DateRange = DateRange.ALL,
    val priceRange: PriceRangeFilter = PriceRangeFilter.ALL,
    val availability: AvailabilityFilter = AvailabilityFilter.ALL,
    val sortBy: SortOption = SortOption.DATE_ASC,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * UI Events for Filter Screen
 */
sealed interface FilterUiEvent {
    data class UpdateDateRange(val dateRange: DateRange) : FilterUiEvent
    data class UpdatePriceRange(val priceRange: PriceRangeFilter) : FilterUiEvent
    data class UpdateAvailability(val availability: AvailabilityFilter) : FilterUiEvent
    data class UpdateSortOption(val sortOption: SortOption) : FilterUiEvent
    data object ClearFilters : FilterUiEvent
    data object ApplyFilters : FilterUiEvent
    data object NavigateBack : FilterUiEvent
}

/**
 * Side Effects for Filter Screen
 */
sealed interface FilterEffect {
    data class ApplyFilters(
        val dateRange: DateRange,
        val priceRange: PriceRangeFilter,
        val availability: AvailabilityFilter,
        val sortBy: SortOption
    ) : FilterEffect
    data object NavigateBack : FilterEffect
    data class ShowError(val message: String) : FilterEffect
}
