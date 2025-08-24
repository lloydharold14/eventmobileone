package com.eventsmobileone.locationsearch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eventsmobileone.components.EventCard
import com.eventsmobileone.components.LocationSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(
    viewModel: LocationSearchViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle effects
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LocationSearchEffect.NavigateToEventDetail -> {
                    // FIXME(eventMO-1017): Navigate to event detail - will be implemented with Decompose navigation
                    println("Navigate to event detail: ${effect.eventId}")
                }
                is LocationSearchEffect.RequestLocationPermission -> {
                    // FIXME(eventMO-1018): Request location permission - will be implemented with platform-specific permission handling
                    println("Request location permission")
                }
                is LocationSearchEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is LocationSearchEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Location Search") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Navigate back */ }) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location search bar
            item {
                LocationSearchBar(
                    locationQuery = state.locationQuery,
                    onLocationQueryChange = { viewModel.onEvent(LocationSearchUiEvent.UpdateLocationQuery(it)) },
                    selectedRadius = state.selectedRadius,
                    onRadiusChange = { viewModel.onEvent(LocationSearchUiEvent.UpdateSearchRadius(it)) },
                    onNearMeClick = { viewModel.onEvent(LocationSearchUiEvent.SearchNearMe) },
                    onSearchClick = { viewModel.onEvent(LocationSearchUiEvent.SearchByLocation) },
                    isLocationPermissionGranted = state.isLocationPermissionGranted,
                    isLoading = state.isLoading
                )
            }
            
            // Search history (if any)
            if (state.searchHistory.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Searches",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                items(state.searchHistory.take(5)) { historyItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = historyItem,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(LocationSearchUiEvent.UpdateLocationQuery(historyItem))
                                    viewModel.onEvent(LocationSearchUiEvent.SearchByLocation)
                                }
                            ) {
                                Text("Use")
                            }
                        }
                    }
                }
            }
            
            // Results section
            if (state.eventsWithDistance.isNotEmpty()) {
                item {
                    Text(
                        text = "Events Found (${state.eventsWithDistance.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                items(state.eventsWithDistance) { eventWithDistance ->
                    EventCard(
                        event = eventWithDistance.event,
                        distanceDisplay = eventWithDistance.getDistanceDisplay(),
                        onClick = { viewModel.onEvent(LocationSearchUiEvent.SelectEvent(eventWithDistance.event.id)) }
                    )
                }
            }
            
            // Empty state
            if (!state.isLoading && state.eventsWithDistance.isEmpty() && state.locationQuery.isNotBlank()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "📍",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Text(
                                text = "No events found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Try adjusting your search location or radius",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            
            // Loading state
            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
