package com.eventsmobileone.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eventsmobileone.Event
import com.eventsmobileone.EventCategory
import com.eventsmobileone.SortOption
import com.eventsmobileone.components.CategoryChipCompact
import com.eventsmobileone.components.EventCard
import com.eventsmobileone.components.EventSearchBar

/**
 * Main Events Screen - Browse Events by Category
 * Implements eventMO-1001 user story
 */
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
            // Header
            EventsHeader(
                searchQuery = state.searchQuery,
                onSearchQueryChange = { viewModel.onEvent(EventsUiEvent.UpdateSearchQuery(it)) },
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
            
            // Sort options
            SortSection(
                currentSort = state.sortBy,
                onSortChanged = { sortOption ->
                    viewModel.onEvent(EventsUiEvent.UpdateSortOption(sortOption))
                }
            )
            
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
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "Discover Events",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
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
    categories: List<EventCategory>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // "All" category
            item {
                CategoryChipCompact(
                    name = "All",
                    icon = "🎯",
                    isSelected = selectedCategoryId == null,
                    onClick = { onCategorySelected(null) }
                )
            }
            
            // Category chips
            items(categories) { category ->
                CategoryChipCompact(
                    name = category.name,
                    icon = category.icon,
                    isSelected = category.id == selectedCategoryId,
                    onClick = { onCategorySelected(category.id) }
                )
            }
        }
    }
}

@Composable
private fun SortSection(
    currentSort: SortOption,
    onSortChanged: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Sort by:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Simple sort options as text buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SortButton(
                text = "Date",
                isSelected = currentSort == SortOption.DATE_ASC,
                onClick = { onSortChanged(SortOption.DATE_ASC) }
            )
            
            SortButton(
                text = "Price",
                isSelected = currentSort == SortOption.PRICE_ASC,
                onClick = { onSortChanged(SortOption.PRICE_ASC) }
            )
            
            SortButton(
                text = "Name",
                isSelected = currentSort == SortOption.NAME_ASC,
                onClick = { onSortChanged(SortOption.NAME_ASC) }
            )
        }
    }
}

@Composable
private fun SortButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isSelected) Color(0xFF9C27B0) else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun EventsList(
    events: List<Event>,
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
                // Initial loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF9C27B0)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading events...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            error != null -> {
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
                            attendeeCount = (event.capacity - event.availableTickets) / 100, // Mock attendee count
                            rating = 4.5f, // Mock rating
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
