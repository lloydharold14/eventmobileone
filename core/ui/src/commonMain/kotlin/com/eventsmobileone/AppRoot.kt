package com.eventsmobileone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eventsmobileone.components.EventButton
import com.eventsmobileone.components.EventButtonSize
import com.eventsmobileone.components.EventButtonVariant
import com.eventsmobileone.components.EventFilterButton
import com.eventsmobileone.components.EventSearchBar

/**
 * Main App Root Component
 * Showcasing the EventMO Design System with Purple Theme
 * Supports both Dark and Light themes
 */
@Composable
fun AppRoot() {
    var isDarkTheme by remember { mutableStateOf(true) }
    
    EventTheme(darkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Theme Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { isDarkTheme = !isDarkTheme }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isDarkTheme) "🌙 Dark" else "☀️ Light",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Header
            Text(
                text = "EventMO Design System",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.size(24.dp))
            
            Text(
                text = "Purple Theme - Based on Behance Design",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.size(32.dp))
            
            // Search Bar Demo
            Text(
                text = "Search Bar",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            var searchText by remember { mutableStateOf("") }
            EventSearchBar(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = "Discover events..."
            )
            
            Spacer(modifier = Modifier.size(32.dp))
            
            // Filter Buttons Demo
            Text(
                text = "Filter Buttons",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EventFilterButton(
                    text = "Discover",
                    onClick = { },
                    isSelected = true
                )
                
                EventFilterButton(
                    text = "Category",
                    onClick = { }
                )
                
                EventFilterButton(
                    text = "Sort",
                    onClick = { }
                )
            }
            
            Spacer(modifier = Modifier.size(32.dp))
            
            // Buttons Demo
            Text(
                text = "Buttons",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EventButton(
                    text = "Primary Button",
                    onClick = { },
                    variant = EventButtonVariant.Primary,
                    size = EventButtonSize.Medium
                )
                
                EventButton(
                    text = "Secondary Button",
                    onClick = { },
                    variant = EventButtonVariant.Secondary,
                    size = EventButtonSize.Medium
                )
                
                EventButton(
                    text = "Large Button",
                    onClick = { },
                    variant = EventButtonVariant.Primary,
                    size = EventButtonSize.Large
                )
                
                EventButton(
                    text = "Disabled Button",
                    onClick = { },
                    enabled = false,
                    variant = EventButtonVariant.Primary,
                    size = EventButtonSize.Medium
                )
            }
            
            Spacer(modifier = Modifier.size(32.dp))
            
            // Theme Colors Demo
            Text(
                text = "Theme Colors",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ColorSwatch("Primary", MaterialTheme.colorScheme.primary)
                ColorSwatch("Secondary", MaterialTheme.colorScheme.secondary)
                ColorSwatch("Surface", MaterialTheme.colorScheme.surface)
                ColorSwatch("Background", MaterialTheme.colorScheme.background)
                ColorSwatch("Surface Variant", MaterialTheme.colorScheme.surfaceVariant)
            }
            
            Spacer(modifier = Modifier.size(32.dp))
            
            // Purple Color Palette Demo
            Text(
                text = "Purple Color Palette",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ColorSwatch("Purple500 (Primary)", Color(0xFF9C27B0))
                ColorSwatch("Purple400", Color(0xFFAB47BC))
                ColorSwatch("Purple300", Color(0xFFBA68C8))
                ColorSwatch("Purple200", Color(0xFFCE93D8))
                ColorSwatch("Purple100", Color(0xFFE1BEE7))
            }
            
            Spacer(modifier = Modifier.size(32.dp))
            
            // Typography Demo
            Text(
                text = "Typography Scale",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Title Large",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Body Large",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Label Medium",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
private fun ColorSwatch(name: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = color,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
        )
        
        Spacer(modifier = Modifier.size(4.dp))
        
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
