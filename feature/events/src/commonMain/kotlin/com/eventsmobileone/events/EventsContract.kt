package com.eventsmobileone.events

import com.eventsmobileone.*

/**
 * UI State for Events Screen
 */
data class EventsUiState(
    val events: List<Event> = emptyList(),
    val categories: List<EventCategory> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    
    // Filter state
    val selectedCategoryId: String? = null,
    val searchQuery: String = "",
    val sortBy: SortOption = SortOption.DATE_ASC,
    val dateRange: DateRange = DateRange.ALL,
    val priceRange: PriceRangeFilter = PriceRangeFilter.ALL,
    val availability: AvailabilityFilter = AvailabilityFilter.ALL,
    
    // Filter UI state
    val activeFiltersCount: Int = 0
)

/**
 * UI Events for Events Screen
 */
sealed interface EventsUiEvent {
    data object LoadCategories : EventsUiEvent
    data object LoadEvents : EventsUiEvent
    data object RefreshEvents : EventsUiEvent
    data class SelectCategory(val categoryId: String?) : EventsUiEvent
    data class UpdateSearchQuery(val query: String) : EventsUiEvent
    data class UpdateSortOption(val sortOption: SortOption) : EventsUiEvent
    data class UpdateDateRange(val dateRange: DateRange) : EventsUiEvent
    data class UpdatePriceRange(val priceRange: PriceRangeFilter) : EventsUiEvent
    data class UpdateAvailability(val availability: AvailabilityFilter) : EventsUiEvent
    data object OpenFilterScreen : EventsUiEvent
    data object OpenLocationSearch : EventsUiEvent
    data object ClearFilters : EventsUiEvent
    data class NavigateToEvent(val eventId: String) : EventsUiEvent
    data object ClearError : EventsUiEvent
}

/**
 * Side Effects for Events Screen
 */
sealed interface EventsEffect {
    data class NavigateToEventDetail(val eventId: String) : EventsEffect
    data object NavigateToFilterScreen : EventsEffect
    data object NavigateToLocationSearch : EventsEffect
    data class ShowError(val message: String) : EventsEffect
}
