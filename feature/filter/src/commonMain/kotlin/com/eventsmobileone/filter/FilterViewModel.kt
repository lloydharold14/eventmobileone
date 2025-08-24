package com.eventsmobileone.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventsmobileone.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FilterViewModel(
    initialFilters: EventFilter? = null
) : ViewModel() {

    private val _state = MutableStateFlow(
        FilterUiState(
            dateRange = initialFilters?.dateRange ?: DateRange.ALL,
            priceRange = initialFilters?.priceRange ?: PriceRangeFilter.ALL,
            availability = initialFilters?.availability ?: AvailabilityFilter.ALL,
            sortBy = initialFilters?.sortBy ?: SortOption.DATE_ASC
        )
    )
    val state: StateFlow<FilterUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FilterEffect>()
    val effect: Flow<FilterEffect> = _effect.asSharedFlow()

    fun onEvent(event: FilterUiEvent) {
        when (event) {
            is FilterUiEvent.UpdateDateRange -> updateDateRange(event.dateRange)
            is FilterUiEvent.UpdatePriceRange -> updatePriceRange(event.priceRange)
            is FilterUiEvent.UpdateAvailability -> updateAvailability(event.availability)
            is FilterUiEvent.UpdateSortOption -> updateSortOption(event.sortOption)
            is FilterUiEvent.ClearFilters -> clearFilters()
            is FilterUiEvent.ApplyFilters -> applyFilters()
            is FilterUiEvent.NavigateBack -> navigateBack()
        }
    }

    private fun updateDateRange(dateRange: DateRange) {
        _state.update { it.copy(dateRange = dateRange) }
    }

    private fun updatePriceRange(priceRange: PriceRangeFilter) {
        _state.update { it.copy(priceRange = priceRange) }
    }

    private fun updateAvailability(availability: AvailabilityFilter) {
        _state.update { it.copy(availability = availability) }
    }

    private fun updateSortOption(sortOption: SortOption) {
        _state.update { it.copy(sortBy = sortOption) }
    }

    private fun clearFilters() {
        _state.update { 
            it.copy(
                dateRange = DateRange.ALL,
                priceRange = PriceRangeFilter.ALL,
                availability = AvailabilityFilter.ALL,
                sortBy = SortOption.DATE_ASC
            )
        }
    }

    private fun applyFilters() {
        viewModelScope.launch {
            val currentState = _state.value
            _effect.emit(
                FilterEffect.ApplyFilters(
                    dateRange = currentState.dateRange,
                    priceRange = currentState.priceRange,
                    availability = currentState.availability,
                    sortBy = currentState.sortBy
                )
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.emit(FilterEffect.NavigateBack)
        }
    }
}
