package com.eventsmobileone.events

import androidx.compose.runtime.Immutable
import com.eventsmobileone.Event
import com.eventsmobileone.EventCategory
import com.eventsmobileone.SortOption

/**
 * Events Screen UI State, Events, and Effects following MVI pattern
 */

@Immutable
data class EventsUiState(
    val isLoading: Boolean = false,
    val categories: List<EventCategory> = emptyList(),
    val events: List<Event> = emptyList(),
    val selectedCategoryId: String? = null,
    val searchQuery: String = "",
    val sortBy: SortOption = SortOption.DATE_ASC,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

sealed interface EventsUiEvent {
    data object LoadCategories : EventsUiEvent
    data object LoadEvents : EventsUiEvent
    data object RefreshEvents : EventsUiEvent
    data class SelectCategory(val categoryId: String?) : EventsUiEvent
    data class UpdateSearchQuery(val query: String) : EventsUiEvent
    data class UpdateSortOption(val sortOption: SortOption) : EventsUiEvent
    data class NavigateToEvent(val eventId: String) : EventsUiEvent
    data object ClearError : EventsUiEvent
}

sealed interface EventsEffect {
    data class NavigateToEventDetail(val eventId: String) : EventsEffect
    data class ShowError(val message: String) : EventsEffect
}
