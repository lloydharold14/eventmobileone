package com.eventsmobileone

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eventsmobileone.events.EventsScreen
import com.eventsmobileone.events.EventsViewModel
import com.eventsmobileone.filter.FilterScreen
import com.eventsmobileone.filter.FilterViewModel
import com.eventsmobileone.locationsearch.LocationSearchScreen
import com.eventsmobileone.locationsearch.LocationSearchViewModel
import com.eventsmobileone.usecase.SearchEventsByLocationUseCase
import com.eventsmobileone.AppTheme
import com.eventsmobileone.repository.MockEventsRepository
import com.eventsmobileone.repository.MockLocationService
import com.eventsmobileone.usecase.GetCategoriesUseCase
import com.eventsmobileone.usecase.GetEventsUseCase

/**
 * Main App Root Component
 * Showcasing the EventMO Events Browsing Feature with Enhanced Filtering (eventMO-1002)
 */
@Composable
fun AppRoot() {
    AppTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Events) }
        var currentFilters by remember { mutableStateOf<com.eventsmobileone.EventFilter?>(null) }
        
        val eventsViewModel = remember {
            EventsViewModel(
                getCategoriesUseCase = GetCategoriesUseCase(
                    eventsRepository = MockEventsRepository()
                ),
                getEventsUseCase = GetEventsUseCase(
                    eventsRepository = MockEventsRepository()
                )
            )
        }
        
        val filterViewModel = remember {
            FilterViewModel(initialFilters = currentFilters)
        }
        
        val locationSearchViewModel = remember {
            LocationSearchViewModel(
                searchEventsByLocationUseCase = SearchEventsByLocationUseCase(
                    eventsRepository = MockEventsRepository(),
                    locationService = MockLocationService()
                ),
                locationService = MockLocationService()
            )
        }
        
        // Handle navigation from Events screen
        LaunchedEffect(eventsViewModel) {
            eventsViewModel.effect.collect { effect ->
                when (effect) {
                    is com.eventsmobileone.events.EventsEffect.NavigateToFilterScreen -> {
                        currentScreen = Screen.Filter
                    }
                    is com.eventsmobileone.events.EventsEffect.NavigateToLocationSearch -> {
                        currentScreen = Screen.LocationSearch
                    }
                    is com.eventsmobileone.events.EventsEffect.NavigateToEventDetail -> {
                        // FIXME(eventMO-1009): Navigate to event detail - will be implemented with Decompose navigation
                        println("Navigate to event detail: ${effect.eventId}")
                    }
                    is com.eventsmobileone.events.EventsEffect.ShowError -> {
                        // FIXME(eventMO-1014): Show error - will be implemented with error handling
                        println("Show error: ${effect.message}")
                    }
                }
            }
        }
        
        // Handle navigation from Filter screen
        LaunchedEffect(filterViewModel) {
            filterViewModel.effect.collect { effect ->
                when (effect) {
                    is com.eventsmobileone.filter.FilterEffect.ApplyFilters -> {
                        // Update current filters and go back to events
                        currentFilters = com.eventsmobileone.EventFilter(
                            dateRange = effect.dateRange,
                            priceRange = effect.priceRange,
                            availability = effect.availability,
                            sortBy = effect.sortBy
                        )
                        currentScreen = Screen.Events
                    }
                    is com.eventsmobileone.filter.FilterEffect.NavigateBack -> {
                        currentScreen = Screen.Events
                    }
                    is com.eventsmobileone.filter.FilterEffect.ShowError -> {
                        // FIXME(eventMO-1013): Show error - will be implemented with error handling
                        println("Show error: ${effect.message}")
                    }
                }
            }
        }
        
        when (currentScreen) {
            Screen.Events -> {
                EventsScreen(viewModel = eventsViewModel)
            }
            Screen.Filter -> {
                FilterScreen(viewModel = filterViewModel)
            }
            Screen.LocationSearch -> {
                LocationSearchScreen(viewModel = locationSearchViewModel)
            }
        }
    }
}

sealed class Screen {
    data object Events : Screen()
    data object Filter : Screen()
    data object LocationSearch : Screen()
}
