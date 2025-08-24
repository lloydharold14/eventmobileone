package com.eventsmobileone.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventsmobileone.*
import com.eventsmobileone.usecase.GetCategoriesUseCase
import com.eventsmobileone.usecase.GetEventsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventsViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EventsUiState())
    val state: StateFlow<EventsUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<EventsEffect>()
    val effect: Flow<EventsEffect> = _effect.asSharedFlow()

    init {
        onEvent(EventsUiEvent.LoadCategories)
        onEvent(EventsUiEvent.LoadEvents)
    }

    fun onEvent(event: EventsUiEvent) {
        when (event) {
            is EventsUiEvent.LoadCategories -> loadCategories()
            is EventsUiEvent.LoadEvents -> loadEvents()
            is EventsUiEvent.RefreshEvents -> refreshEvents()
            is EventsUiEvent.SelectCategory -> selectCategory(event.categoryId)
            is EventsUiEvent.UpdateSearchQuery -> updateSearchQuery(event.query)
            is EventsUiEvent.UpdateSortOption -> updateSortOption(event.sortOption)
            is EventsUiEvent.UpdateDateRange -> updateDateRange(event.dateRange)
            is EventsUiEvent.UpdatePriceRange -> updatePriceRange(event.priceRange)
            is EventsUiEvent.UpdateAvailability -> updateAvailability(event.availability)
            is EventsUiEvent.OpenFilterScreen -> openFilterScreen()
            is EventsUiEvent.OpenLocationSearch -> openLocationSearch()
            is EventsUiEvent.ClearFilters -> clearFilters()
            is EventsUiEvent.NavigateToEvent -> navigateToEvent(event.eventId)
            is EventsUiEvent.ClearError -> clearError()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getCategoriesUseCase()
                .onSuccess { categories ->
                    _state.update { 
                        it.copy(
                            categories = categories,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update { 
                        it.copy(
                            error = throwable.message ?: "Failed to load categories",
                            isLoading = false
                        )
                    }
                    _effect.emit(EventsEffect.ShowError(throwable.message ?: "Failed to load categories"))
                }
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            val currentState = _state.value
            _state.update { it.copy(isLoading = true, error = null) }
            
            val filter = createEventFilter(currentState)
            
            getEventsUseCase(filter)
                .onSuccess { events ->
                    _state.update { 
                        it.copy(
                            events = events,
                            isLoading = false,
                            activeFiltersCount = calculateActiveFiltersCount(it)
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update { 
                        it.copy(
                            error = throwable.message ?: "Failed to load events",
                            isLoading = false
                        )
                    }
                    _effect.emit(EventsEffect.ShowError(throwable.message ?: "Failed to load events"))
                }
        }
    }

    private fun refreshEvents() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            
            val currentState = _state.value
            val filter = createEventFilter(currentState)
            
            getEventsUseCase(filter)
                .onSuccess { events ->
                    _state.update { 
                        it.copy(
                            events = events,
                            isRefreshing = false,
                            activeFiltersCount = calculateActiveFiltersCount(it)
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update { 
                        it.copy(
                            error = throwable.message ?: "Failed to refresh events",
                            isRefreshing = false
                        )
                    }
                    _effect.emit(EventsEffect.ShowError(throwable.message ?: "Failed to refresh events"))
                }
        }
    }

    private fun selectCategory(categoryId: String?) {
        _state.update { it.copy(selectedCategoryId = categoryId) }
        loadEvents()
    }

    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        loadEvents()
    }

    private fun updateSortOption(sortOption: SortOption) {
        _state.update { it.copy(sortBy = sortOption) }
        loadEvents()
    }

    private fun updateDateRange(dateRange: DateRange) {
        _state.update { it.copy(dateRange = dateRange) }
        loadEvents()
    }

    private fun updatePriceRange(priceRange: PriceRangeFilter) {
        _state.update { it.copy(priceRange = priceRange) }
        loadEvents()
    }

    private fun updateAvailability(availability: AvailabilityFilter) {
        _state.update { it.copy(availability = availability) }
        loadEvents()
    }

    private fun openFilterScreen() {
        viewModelScope.launch {
            _effect.emit(EventsEffect.NavigateToFilterScreen)
        }
    }
    
    private fun openLocationSearch() {
        viewModelScope.launch {
            _effect.emit(EventsEffect.NavigateToLocationSearch)
        }
    }

    private fun clearFilters() {
        _state.update { 
            it.copy(
                selectedCategoryId = null,
                searchQuery = "",
                sortBy = SortOption.DATE_ASC,
                dateRange = DateRange.ALL,
                priceRange = PriceRangeFilter.ALL,
                availability = AvailabilityFilter.ALL,
                activeFiltersCount = 0
            )
        }
        loadEvents()
    }

    private fun navigateToEvent(eventId: String) {
        viewModelScope.launch {
            _effect.emit(EventsEffect.NavigateToEventDetail(eventId))
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun createEventFilter(state: EventsUiState): EventFilter {
        return EventFilter(
            categoryId = state.selectedCategoryId,
            searchQuery = state.searchQuery,
            sortBy = state.sortBy,
            dateRange = state.dateRange,
            priceRange = state.priceRange,
            availability = state.availability
        )
    }

    // Computed property for active filters count
    private fun calculateActiveFiltersCount(state: EventsUiState): Int {
        var count = 0
        if (state.selectedCategoryId != null) count++
        if (state.searchQuery.isNotBlank()) count++
        if (state.dateRange != DateRange.ALL) count++
        if (state.priceRange != PriceRangeFilter.ALL) count++
        if (state.availability != AvailabilityFilter.ALL) count++
        return count
    }
}
