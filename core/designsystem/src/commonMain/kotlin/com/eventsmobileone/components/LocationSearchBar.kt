package com.eventsmobileone.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eventsmobileone.SearchRadius

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchBar(
    locationQuery: String,
    onLocationQueryChange: (String) -> Unit,
    selectedRadius: SearchRadius,
    onRadiusChange: (SearchRadius) -> Unit,
    onNearMeClick: () -> Unit,
    onSearchClick: () -> Unit,
    isLocationPermissionGranted: Boolean = false,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Location search field
        OutlinedTextField(
            value = locationQuery,
            onValueChange = onLocationQueryChange,
            placeholder = { Text("Enter location (e.g., New York, NY)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF9C27B0),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true,
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF9C27B0)
                    )
                } else {
                    Text(
                        text = "🔍",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
        
        // Search radius selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Radius:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            
            SearchRadius.values().forEach { radius ->
                FilterChip(
                    selected = selectedRadius == radius,
                    onClick = { onRadiusChange(radius) },
                    label = { Text(radius.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF9C27B0),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Near Me button
            OutlinedButton(
                onClick = onNearMeClick,
                modifier = Modifier.weight(1f),
                enabled = isLocationPermissionGranted,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = if (isLocationPermissionGranted) "📍 Near Me" else "📍 Enable Location",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // Search button
            Button(
                onClick = onSearchClick,
                modifier = Modifier.weight(1f),
                enabled = locationQuery.isNotBlank() || isLocationPermissionGranted,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                )
            ) {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
