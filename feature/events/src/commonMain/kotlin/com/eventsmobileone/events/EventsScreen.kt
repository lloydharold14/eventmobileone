package com.eventsmobileone.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
                onFilterClick = { viewModel.onEvent(EventsUiEvent.ToggleFilters) },
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
            
            // Filters section
            if (state.showFilters) {
                FiltersSection(
                    dateRange = state.dateRange,
                    priceRange = state.priceRange,
                    availability = state.availability,
                    sortBy = state.sortBy,
                    onDateRangeChanged = { viewModel.onEvent(EventsUiEvent.UpdateDateRange(it)) },
                    onPriceRangeChanged = { viewModel.onEvent(EventsUiEvent.UpdatePriceRange(it)) },
                    onAvailabilityChanged = { viewModel.onEvent(EventsUiEvent.UpdateAvailability(it)) },
                    onSortChanged = { viewModel.onEvent(EventsUiEvent.UpdateSortOption(it)) },
                    onClearFilters = { viewModel.onEvent(EventsUiEvent.ClearFilters) }
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
            
            // Filter button with badge
            Box {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filters",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                
                // Badge for active filters
                if (activeFiltersCount > 0) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd),
                        containerColor = Color(0xFF9C27B0)
                    ) {
                        Text(
                            text = activeFiltersCount.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        EventSearchBar(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = "Search events..."
        )
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
private fun FiltersSection(
    dateRange: com.eventsmobileone.DateRange,
    priceRange: com.eventsmobileone.PriceRangeFilter,
    availability: com.eventsmobileone.AvailabilityFilter,
    sortBy: com.eventsmobileone.SortOption,
    onDateRangeChanged: (com.eventsmobileone.DateRange) -> Unit,
    onPriceRangeChanged: (com.eventsmobileone.PriceRangeFilter) -> Unit,
    onAvailabilityChanged: (com.eventsmobileone.AvailabilityFilter) -> Unit,
    onSortChanged: (com.eventsmobileone.SortOption) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            TextButton(onClick = onClearFilters) {
                Text("Clear All")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Date Range Filter
        Text(
            text = "Date",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(com.eventsmobileone.DateRange.values()) { range ->
                FilterChip(
                    selected = dateRange == range,
                    onClick = { onDateRangeChanged(range) },
                    label = { Text(range.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF9C27B0),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Price Range Filter
        Text(
            text = "Price",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(com.eventsmobileone.PriceRangeFilter.values()) { range ->
                FilterChip(
                    selected = priceRange == range,
                    onClick = { onPriceRangeChanged(range) },
                    label = { Text(range.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF9C27B0),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Availability Filter
        Text(
            text = "Availability",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(com.eventsmobileone.AvailabilityFilter.values()) { availability ->
                FilterChip(
                    selected = this@FiltersSection.availability == availability,
                    onClick = { onAvailabilityChanged(availability) },
                    label = { Text(availability.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF9C27B0),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sort Options
        Text(
            text = "Sort By",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(com.eventsmobileone.SortOption.values()) { sortOption ->
                FilterChip(
                    selected = sortBy == sortOption,
                    onClick = { onSortChanged(sortOption) },
                    label = { Text(sortOption.displayName) },
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
                            date = event.date,
                            location = event.location,
                            venue = event.venue,
                            priceText = event.priceRange.getDisplayText(),
                            availableTickets = event.availableTickets,
                            capacity = event.capacity,
                            organizer = event.organizer,
                            imageUrl = event.thumbnailUrl,
                            isFeatured = event.isFeatured,
                            attendeeCount = (event.capacity - event.availableTickets) / 100,
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
