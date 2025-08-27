package com.eventsmobileone.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eventsmobileone.Event
import com.eventsmobileone.EventCategory
import com.eventsmobileone.components.*
import com.eventsmobileone.designsystem.*

@Composable
fun ModernEventsScreen(
    events: List<Event>,
    categories: List<EventCategory>,
    selectedCategory: EventCategory?,
    searchQuery: String,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onEventClick: (Event) -> Unit,
    onCategorySelect: (EventCategory?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFilterClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Section
            ModernEventsHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearch = onSearch,
                onFilterClick = onFilterClick
            )
            
            // Categories Section
            if (categories.isNotEmpty()) {
                ModernCategoriesSection(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelect = onCategorySelect
                )
            }
            
            // Events List
            ModernEventsList(
                events = events,
                isLoading = isLoading,
                isRefreshing = isRefreshing,
                onEventClick = onEventClick,
                onRefresh = onRefresh
            )
        }
    }
}

@Composable
private fun ModernEventsHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.md),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        // Title
        Text(
            text = "Discover Events",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // Search and Filter Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModernSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onSearch = onSearch,
                placeholder = "Search events, locations...",
                modifier = Modifier.weight(1f)
            )
            
            ModernFilterButton(
                onClick = onFilterClick,
                filterCount = 0, // TODO: Calculate active filters
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ModernCategoriesSection(
    categories: List<EventCategory>,
    selectedCategory: EventCategory?,
    onCategorySelect: (EventCategory?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.md),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
    ) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            contentPadding = PaddingValues(vertical = AppSpacing.xs)
        ) {
            // All Events option
            item {
                ModernCategoryChip(
                    category = EventCategory("all", "All Events", "🏠", "#666666"),
                    isSelected = selectedCategory == null,
                    onClick = { onCategorySelect(null) }
                )
            }
            
            // Category options
            items(categories) { category ->
                ModernCategoryChip(
                    category = category,
                    isSelected = selectedCategory?.id == category.id,
                    onClick = { onCategorySelect(category) }
                )
            }
        }
    }
}

@Composable
private fun ModernEventsList(
    events: List<Event>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onEventClick: (Event) -> Unit,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.md)
    ) {
        when {
            isLoading && events.isEmpty() -> {
                // Loading state
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
                    contentPadding = PaddingValues(vertical = AppSpacing.md)
                ) {
                    items(5) {
                        ModernEventCardShimmer()
                    }
                }
            }
            
            events.isEmpty() -> {
                // Empty state
                ModernEmptyState(
                    title = "No Events Found",
                    message = "Try adjusting your search or filters to discover amazing events near you.",
                    icon = "🎉",
                    actionText = "Clear Filters",
                    onAction = { /* TODO: Clear filters */ }
                )
            }
            
            else -> {
                // Events list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
                    contentPadding = PaddingValues(vertical = AppSpacing.md)
                ) {
                    items(
                        items = events,
                        key = { it.id }
                    ) { event ->
                        ModernEventCard(
                            event = event,
                            onClick = { onEventClick(event) }
                        )
                    }
                }
            }
        }
        
        // Pull to refresh indicator
        ModernPullToRefreshIndicator(
            isRefreshing = isRefreshing,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

// Modern Event Detail Header Component
@Composable
fun ModernEventDetailHeader(
    event: Event,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = AppElevation.md,
                shape = RoundedCornerShape(bottomStart = AppRadius.lg, bottomEnd = AppRadius.lg)
            ),
        shape = RoundedCornerShape(bottomStart = AppRadius.lg, bottomEnd = AppRadius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            // Event Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📅",
                    style = MaterialTheme.typography.displayLarge
                )
            }
            
            // Back and Share buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Card(
                    modifier = Modifier.clickable { onBackClick() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(AppRadius.lg)
                ) {
                    Text(
                        text = "←",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(AppSpacing.sm)
                    )
                }
                
                Card(
                    modifier = Modifier.clickable { onShareClick() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(AppRadius.lg)
                ) {
                    Text(
                        text = "📤",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(AppSpacing.sm)
                    )
                }
            }
            
            // Event Info Overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(AppSpacing.md)
            ) {
                // Category Badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(AppRadius.sm)
                ) {
                    Text(
                        text = event.categoryName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
                    )
                }
                
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                
                // Event Title
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Price
                Text(
                                            text = "${event.pricing.currency} ${event.pricing.basePrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
