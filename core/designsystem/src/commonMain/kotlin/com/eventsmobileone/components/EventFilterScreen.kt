package com.eventsmobileone.components

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
import com.eventsmobileone.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFilterScreen(
    dateRange: DateRange,
    priceRange: PriceRangeFilter,
    availability: AvailabilityFilter,
    sortBy: SortOption,
    onDateRangeChanged: (DateRange) -> Unit,
    onPriceRangeChanged: (PriceRangeFilter) -> Unit,
    onAvailabilityChanged: (AvailabilityFilter) -> Unit,
    onSortChanged: (SortOption) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onClearFilters) {
                        Text("Clear All")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onClearFilters,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reset")
                    }
                    
                    Button(
                        onClick = onApplyFilters,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9C27B0)
                        )
                    ) {
                        Text("Apply Filters")
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Date Range Filter
            item {
                FilterSection(
                    title = "Date",
                    subtitle = "When do you want to attend?",
                    content = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(DateRange.values()) { range ->
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
                    }
                )
            }
            
            // Price Range Filter
            item {
                FilterSection(
                    title = "Price",
                    subtitle = "What's your budget?",
                    content = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(PriceRangeFilter.values()) { range ->
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
                    }
                )
            }
            
            // Availability Filter
            item {
                FilterSection(
                    title = "Availability",
                    subtitle = "Show events with available tickets",
                    content = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(AvailabilityFilter.values()) { filterAvailability ->
                                FilterChip(
                                    selected = availability == filterAvailability,
                                    onClick = { onAvailabilityChanged(filterAvailability) },
                                    label = { Text(filterAvailability.displayName) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF9C27B0),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                )
            }
            
            // Sort Options
            item {
                FilterSection(
                    title = "Sort By",
                    subtitle = "How should we organize the results?",
                    content = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(SortOption.values()) { sortOption ->
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
                )
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        content()
    }
}
