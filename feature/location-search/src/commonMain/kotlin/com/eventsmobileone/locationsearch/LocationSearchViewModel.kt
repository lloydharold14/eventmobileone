package com.eventsmobileone.locationsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventsmobileone.*
import com.eventsmobileone.usecase.SearchEventsByLocationUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LocationSearchViewModel(
    private val searchEventsByLocationUseCase: SearchEventsByLocationUseCase,
    private val locationService: LocationService
) : ViewModel() {

    private val _state = MutableStateFlow(LocationSearchUiState())
    val state: StateFlow<LocationSearchUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LocationSearchEffect>()
    val effect: Flow<LocationSearchEffect> = _effect.asSharedFlow()

    init {
        checkLocationPermission()
        loadSearchHistory()
    }

    fun onEvent(event: LocationSearchUiEvent) {
        when (event) {
            is LocationSearchUiEvent.UpdateLocationQuery -> {
                updateLocationQuery(event.query)
            }
            is LocationSearchUiEvent.UpdateSearchRadius -> {
                updateSearchRadius(event.radius)
            }
            is LocationSearchUiEvent.SearchByLocation -> {
                searchByLocation()
            }
            is LocationSearchUiEvent.SearchNearMe -> {
                searchNearMe()
            }
            is LocationSearchUiEvent.RequestLocationPermission -> {
                requestLocationPermission()
            }
            is LocationSearchUiEvent.ClearError -> {
                clearError()
            }
            is LocationSearchUiEvent.ClearSearch -> {
                clearSearch()
            }
            is LocationSearchUiEvent.SelectEvent -> {
                selectEvent(event.eventId)
            }
        }
    }

    private fun updateLocationQuery(query: String) {
        _state.update { it.copy(locationQuery = query) }
    }

    private fun updateSearchRadius(radius: SearchRadius) {
        _state.update { it.copy(selectedRadius = radius) }
    }

    private fun searchByLocation() {
        val currentState = _state.value
        if (currentState.locationQuery.isBlank()) {
            viewModelScope.launch {
                _effect.emit(LocationSearchEffect.ShowError("Please enter a location"))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = searchEventsByLocationUseCase.searchByLocation(
                    locationQuery = currentState.locationQuery,
                    radiusMiles = currentState.selectedRadius.miles
                )
                
                result.fold(
                    onSuccess = { eventsWithDistance ->
                        _state.update {
                            it.copy(
                                eventsWithDistance = eventsWithDistance,
                                isLoading = false
                            )
                        }
                        addToSearchHistory(currentState.locationQuery)
                        if (eventsWithDistance.isEmpty()) {
                            _effect.emit(LocationSearchEffect.ShowSuccess("No events found in this area"))
                        }
                    },
                    onFailure = { exception ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to search events"
                            )
                        }
                        _effect.emit(LocationSearchEffect.ShowError(exception.message ?: "Failed to search events"))
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
                _effect.emit(LocationSearchEffect.ShowError(e.message ?: "An unexpected error occurred"))
            }
        }
    }

    private fun searchNearMe() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = searchEventsByLocationUseCase.searchNearMe(
                    radiusMiles = _state.value.selectedRadius.miles
                )
                
                result.fold(
                    onSuccess = { eventsWithDistance ->
                        _state.update {
                            it.copy(
                                eventsWithDistance = eventsWithDistance,
                                isLoading = false
                            )
                        }
                        if (eventsWithDistance.isEmpty()) {
                            _effect.emit(LocationSearchEffect.ShowSuccess("No events found near you"))
                        }
                    },
                    onFailure = { exception ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to search events"
                            )
                        }
                        _effect.emit(LocationSearchEffect.ShowError(exception.message ?: "Failed to search events"))
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
                _effect.emit(LocationSearchEffect.ShowError(e.message ?: "An unexpected error occurred"))
            }
        }
    }

    private fun requestLocationPermission() {
        viewModelScope.launch {
            try {
                val granted = locationService.requestLocationPermission()
                _state.update { it.copy(isLocationPermissionGranted = granted) }
                
                if (granted) {
                    _effect.emit(LocationSearchEffect.ShowSuccess("Location permission granted"))
                } else {
                    _effect.emit(LocationSearchEffect.ShowError("Location permission denied"))
                }
            } catch (e: Exception) {
                _effect.emit(LocationSearchEffect.ShowError("Failed to request location permission"))
            }
        }
    }

    private fun checkLocationPermission() {
        viewModelScope.launch {
            try {
                val hasPermission = locationService.hasLocationPermission()
                _state.update { it.copy(isLocationPermissionGranted = hasPermission) }
            } catch (e: Exception) {
                // Permission check failed, assume not granted
                _state.update { it.copy(isLocationPermissionGranted = false) }
            }
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun clearSearch() {
        _state.update {
            it.copy(
                locationQuery = "",
                eventsWithDistance = emptyList(),
                error = null
            )
        }
    }

    private fun selectEvent(eventId: String) {
        viewModelScope.launch {
            _effect.emit(LocationSearchEffect.NavigateToEventDetail(eventId))
        }
    }

    private fun addToSearchHistory(query: String) {
        val currentHistory = _state.value.searchHistory.toMutableList()
        if (!currentHistory.contains(query)) {
            currentHistory.add(0, query)
            if (currentHistory.size > 10) {
                currentHistory.removeAt(currentHistory.size - 1)
            }
            _state.update { it.copy(searchHistory = currentHistory) }
            // TODO: Persist search history
        }
    }

    private fun loadSearchHistory() {
        // TODO: Load search history from persistent storage
        _state.update { it.copy(searchHistory = emptyList()) }
    }
}
