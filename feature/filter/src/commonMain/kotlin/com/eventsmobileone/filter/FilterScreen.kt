package com.eventsmobileone.filter

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eventsmobileone.components.EventFilterScreen

@Composable
fun FilterScreen(
    viewModel: FilterViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // Handle effects
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FilterEffect.ApplyFilters -> {
                    // FIXME(eventMO-1011): Apply filters and navigate back - will be implemented with navigation
                    println("Apply filters: $effect")
                }
                is FilterEffect.NavigateBack -> {
                    // FIXME(eventMO-1012): Navigate back - will be implemented with Decompose navigation
                    println("Navigate back from filter screen")
                }
                is FilterEffect.ShowError -> {
                    // FIXME(eventMO-1013): Show error - will be implemented with error handling
                    println("Show error: ${effect.message}")
                }
            }
        }
    }
    
    EventFilterScreen(
        dateRange = state.dateRange,
        priceRange = state.priceRange,
        availability = state.availability,
        sortBy = state.sortBy,
        onDateRangeChanged = { viewModel.onEvent(FilterUiEvent.UpdateDateRange(it)) },
        onPriceRangeChanged = { viewModel.onEvent(FilterUiEvent.UpdatePriceRange(it)) },
        onAvailabilityChanged = { viewModel.onEvent(FilterUiEvent.UpdateAvailability(it)) },
        onSortChanged = { viewModel.onEvent(FilterUiEvent.UpdateSortOption(it)) },
        onClearFilters = { viewModel.onEvent(FilterUiEvent.ClearFilters) },
        onApplyFilters = { viewModel.onEvent(FilterUiEvent.ApplyFilters) },
        onBackClick = { viewModel.onEvent(FilterUiEvent.NavigateBack) },
        modifier = modifier
    )
}
