package com.eventsmobileone.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.eventsmobileone.designsystem.*

// Modern Bottom Navigation Bar
@Composable
fun ModernBottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = AppElevation.lg,
                shape = RoundedCornerShape(topStart = AppRadius.lg, topEnd = AppRadius.lg)
            ),
        shape = RoundedCornerShape(topStart = AppRadius.lg, topEnd = AppRadius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModernBottomNavItem(
                icon = "🏠",
                label = "Home",
                route = "home",
                isSelected = currentRoute == "home",
                onClick = { onNavigate("home") }
            )
            
            ModernBottomNavItem(
                icon = "🔍",
                label = "Search",
                route = "search",
                isSelected = currentRoute == "search",
                onClick = { onNavigate("search") }
            )
            
            ModernBottomNavItem(
                icon = "❤️",
                label = "Favorites",
                route = "favorites",
                isSelected = currentRoute == "favorites",
                onClick = { onNavigate("favorites") }
            )
            
            ModernBottomNavItem(
                icon = "👤",
                label = "Profile",
                route = "profile",
                isSelected = currentRoute == "profile",
                onClick = { onNavigate("profile") }
            )
        }
    }
}

@Composable
private fun ModernBottomNavItem(
    icon: String,
    label: String,
    route: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
    ) {
        Card(
            modifier = Modifier.clickable { onClick() },
            shape = RoundedCornerShape(AppRadius.lg),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                } else {
                    Color.Transparent
                }
            )
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(AppSpacing.sm)
            )
        }
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// Modern Top App Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopAppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    onActionClick: (() -> Unit)? = null,
    actionIcon: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = AppElevation.sm,
                shape = RoundedCornerShape(bottomStart = AppRadius.lg, bottomEnd = AppRadius.lg)
            ),
        shape = RoundedCornerShape(bottomStart = AppRadius.lg, bottomEnd = AppRadius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            if (onBackClick != null) {
                Card(
                    modifier = Modifier.clickable { onBackClick() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(AppRadius.lg)
                ) {
                    Text(
                        text = "←",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(AppSpacing.sm)
                    )
                }
                
                Spacer(modifier = Modifier.width(AppSpacing.md))
            }
            
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            // Action Button
            if (onActionClick != null && actionIcon != null) {
                Card(
                    modifier = Modifier.clickable { onActionClick() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(AppRadius.lg)
                ) {
                    Text(
                        text = actionIcon,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(AppSpacing.sm)
                    )
                }
            }
            
            // Menu Button
            if (onMenuClick != null) {
                Spacer(modifier = Modifier.width(AppSpacing.sm))
                Card(
                    modifier = Modifier.clickable { onMenuClick() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(AppRadius.lg)
                ) {
                    Text(
                        text = "☰",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(AppSpacing.sm)
                    )
                }
            }
        }
    }
}

// Modern Floating Action Button
@Composable
fun ModernFloatingActionButton(
    onClick: () -> Unit,
    icon: String = "➕",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(AppIconSize.xl)
            .clickable { onClick() }
            .shadow(
                elevation = AppElevation.lg,
                shape = RoundedCornerShape(AppRadius.xl)
            ),
        shape = RoundedCornerShape(AppRadius.xl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// Modern Navigation Drawer Item
@Composable
fun ModernNavigationDrawerItem(
    icon: String,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(AppRadius.lg),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            } else {
                Color.Transparent
            }
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

// Modern Tab Bar
@Composable
fun ModernTabBar(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = AppElevation.sm,
                shape = RoundedCornerShape(AppRadius.lg)
            ),
        shape = RoundedCornerShape(AppRadius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.xs),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
        ) {
            tabs.forEachIndexed { index, tab ->
                ModernTabItem(
                    text = tab,
                    isSelected = index == selectedTabIndex,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ModernTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(AppRadius.md),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Transparent
            }
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppSpacing.sm, horizontal = AppSpacing.md)
        )
    }
}

// Modern Breadcrumb Navigation
@Composable
fun ModernBreadcrumbNavigation(
    items: List<String>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
    ) {
        items.forEachIndexed { index, item ->
            if (index > 0) {
                Text(
                    text = ">",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = item,
                style = MaterialTheme.typography.bodyMedium,
                color = if (index == items.size - 1) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontWeight = if (index == items.size - 1) FontWeight.SemiBold else FontWeight.Normal,
                modifier = Modifier.clickable { onItemClick(index) }
            )
        }
    }
}
