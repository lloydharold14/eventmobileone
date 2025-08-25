package com.eventsmobileone.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eventsmobileone.components.EventCard
import com.eventsmobileone.components.EventSearchBar

@Composable
fun EventsScreen(
    viewModel: EventsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle effects
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is EventsEffect.NavigateToEventDetail -> {
                    // FIXME(eventMO-1009): Navigate to event detail - will be implemented with Decompose navigation
                    println("Navigate to event: ${effect.eventId}")
                }
                is EventsEffect.NavigateToFilterScreen -> {
                    // FIXME(eventMO-1010): Navigate to filter screen - will be implemented with Decompose navigation
                    println("Navigate to filter screen")
                }
                is EventsEffect.NavigateToLocationSearch -> {
                    // FIXME(eventMO-1019): Navigate to location search - will be implemented with Decompose navigation
                    println("Navigate to location search")
                }
                is EventsEffect.ShowError -> {
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header with search and filter
            EventsHeader(
                searchQuery = state.searchQuery,
                onSearchQueryChange = { viewModel.onEvent(EventsUiEvent.UpdateSearchQuery(it)) },
                onFilterClick = { viewModel.onEvent(EventsUiEvent.OpenFilterScreen) },
                onLocationSearchClick = { viewModel.onEvent(EventsUiEvent.OpenLocationSearch) },
                activeFiltersCount = state.activeFiltersCount,
                onRefresh = { viewModel.onEvent(EventsUiEvent.RefreshEvents) }
            )
            
            // Categories
            if (state.categories.isNotEmpty()) {
                CategoriesSection(
                    categories = state.categories,
                    selectedCategoryId = state.selectedCategoryId,
                    onCategorySelected = { categoryId ->
                        viewModel.onEvent(EventsUiEvent.SelectCategory(categoryId))
                    }
                )
            }
            
            // Events list
            EventsList(
                events = state.events,
                isLoading = state.isLoading,
                isRefreshing = state.isRefreshing,
                error = state.error,
                onEventClick = { eventId ->
                    viewModel.onEvent(EventsUiEvent.NavigateToEvent(eventId))
                },
                onRefresh = { viewModel.onEvent(EventsUiEvent.RefreshEvents) },
                onClearError = { viewModel.onEvent(EventsUiEvent.ClearError) }
            )
        }
    }
}

@Composable
private fun EventsHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onLocationSearchClick: () -> Unit,
    activeFiltersCount: Int,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Discover Events",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        EventSearchBar(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = "Search events...",
            onFilterClick = onFilterClick,
            activeFiltersCount = activeFiltersCount
        )
        
        // Location search button
        OutlinedButton(
            onClick = onLocationSearchClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = "📍 Search by Location",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CategoriesSection(
    categories: List<com.eventsmobileone.EventCategory>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // All categories option
            item {
                FilterChip(
                    selected = selectedCategoryId == null,
                    onClick = { onCategorySelected(null) },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF9C27B0),
                        selectedLabelColor = Color.White
                    )
                )
            }
            
            // Category chips
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategoryId == category.id,
                    onClick = { onCategorySelected(category.id) },
                    label = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(category.icon)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(category.name)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF9C27B0),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun EventsList(
    events: List<com.eventsmobileone.Event>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String?,
    onEventClick: (String) -> Unit,
    onRefresh: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            isLoading && events.isEmpty() -> {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF9C27B0)
                    )
                }
            }
            
            error != null && events.isEmpty() -> {
                // Error state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "😕",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Something went wrong",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                onClearError()
                                onRefresh()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF9C27B0)
                            )
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }
            
            events.isEmpty() -> {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎪",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No events found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search or category filter",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            else -> {
                // Events list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = events,
                        key = { event -> event.id }
                    ) { event ->
                        EventCard(
                            title = event.title,
                            date = event.startDate,
                            location = "${event.location.address}, ${event.location.city}",
                            venue = event.location.address,
                            priceText = "${event.pricing.displayCurrency} ${event.pricing.displayAmount}",
                            availableTickets = event.currentAttendees,
                            capacity = event.maxAttendees,
                            organizer = event.organizer.name,
                            imageUrl = event.images.firstOrNull(),
                            isFeatured = false, // TODO: Add featured flag to Event model
                            attendeeCount = event.currentAttendees,
                            rating = 4.5f,
                            onClick = { onEventClick(event.id) }
                        )
                    }
                }
            }
        }
        
        // Refreshing indicator
        if (isRefreshing) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                color = Color(0xFF9C27B0)
            )
        }
    }
}
