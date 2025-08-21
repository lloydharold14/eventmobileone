package com.eventsmobileone.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventsmobileone.EventFilter
import com.eventsmobileone.usecase.GetCategoriesUseCase
import com.eventsmobileone.usecase.GetEventsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Events ViewModel following MVI pattern
 */
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
            
            val filter = EventFilter(
                categoryId = currentState.selectedCategoryId,
                searchQuery = currentState.searchQuery,
                sortBy = currentState.sortBy
            )
            
            getEventsUseCase(filter)
                .onSuccess { events ->
                    _state.update { 
                        it.copy(
                            events = events,
                            isLoading = false
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
            val filter = EventFilter(
                categoryId = currentState.selectedCategoryId,
                searchQuery = currentState.searchQuery,
                sortBy = currentState.sortBy
            )
            
            getEventsUseCase(filter)
                .onSuccess { events ->
                    _state.update { 
                        it.copy(
                            events = events,
                            isRefreshing = false
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
        _state.update { 
            it.copy(selectedCategoryId = categoryId)
        }
        loadEvents() // Reload events with new filter
    }

    private fun updateSearchQuery(query: String) {
        _state.update { 
            it.copy(searchQuery = query)
        }
        if (query.isBlank() || query.length >= 2) {
            loadEvents() // Reload events with search filter
        }
    }

    private fun updateSortOption(sortOption: com.eventsmobileone.SortOption) {
        _state.update { 
            it.copy(sortBy = sortOption)
        }
        loadEvents() // Reload events with new sorting
    }

    private fun navigateToEvent(eventId: String) {
        viewModelScope.launch {
            _effect.emit(EventsEffect.NavigateToEventDetail(eventId))
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
