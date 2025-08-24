package com.eventsmobileone.locationsearch

import com.eventsmobileone.*

/**
 * UI State for Location Search Screen
 */
data class LocationSearchUiState(
    val locationQuery: String = "",
    val selectedRadius: SearchRadius = SearchRadius.TWENTY_FIVE_MILES,
    val eventsWithDistance: List<EventWithDistance> = emptyList(),
    val isLoading: Boolean = false,
    val isLocationPermissionGranted: Boolean = false,
    val error: String? = null,
    val searchHistory: List<String> = emptyList()
)

/**
 * UI Events for Location Search Screen
 */
sealed interface LocationSearchUiEvent {
    data class UpdateLocationQuery(val query: String) : LocationSearchUiEvent
    data class UpdateSearchRadius(val radius: SearchRadius) : LocationSearchUiEvent
    data object SearchByLocation : LocationSearchUiEvent
    data object SearchNearMe : LocationSearchUiEvent
    data object RequestLocationPermission : LocationSearchUiEvent
    data object ClearError : LocationSearchUiEvent
    data object ClearSearch : LocationSearchUiEvent
    data class SelectEvent(val eventId: String) : LocationSearchUiEvent
}

/**
 * Side Effects for Location Search Screen
 */
sealed interface LocationSearchEffect {
    data class NavigateToEventDetail(val eventId: String) : LocationSearchEffect
    data object RequestLocationPermission : LocationSearchEffect
    data class ShowError(val message: String) : LocationSearchEffect
    data class ShowSuccess(val message: String) : LocationSearchEffect
}
