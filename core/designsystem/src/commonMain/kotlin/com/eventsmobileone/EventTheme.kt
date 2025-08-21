package com.eventsmobileone

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ============================================================================
// COLOR SYSTEM
// ============================================================================

/**
 * EventMO Design System Colors
 * Based on Behance Event Management App Design with Purple Primary
 */
object EventColors {
    // Primary Purple Palette (replacing yellow/lime green from original)
    val Purple50 = Color(0xFFF3E5F5)
    val Purple100 = Color(0xFFE1BEE7)
    val Purple200 = Color(0xFFCE93D8)
    val Purple300 = Color(0xFFBA68C8)
    val Purple400 = Color(0xFFAB47BC)
    val Purple500 = Color(0xFF9C27B0)  // Primary Purple
    val Purple600 = Color(0xFF8E24AA)
    val Purple700 = Color(0xFF7B1FA2)
    val Purple800 = Color(0xFF6A1B9A)
    val Purple900 = Color(0xFF4A148C)
    
    // Neutral Colors (Dark Theme)
    val Black = Color(0xFF000000)
    val DarkGray900 = Color(0xFF121212)
    val DarkGray800 = Color(0xFF1E1E1E)
    val DarkGray700 = Color(0xFF2D2D2D)
    val DarkGray600 = Color(0xFF404040)
    val DarkGray500 = Color(0xFF666666)
    val DarkGray400 = Color(0xFF8A8A8A)
    val DarkGray300 = Color(0xFFB3B3B3)
    val DarkGray200 = Color(0xFFCCCCCC)
    val DarkGray100 = Color(0xFFE0E0E0)
    val White = Color(0xFFFFFFFF)
    
    // Semantic Colors
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Error = Color(0xFFF44336)
    val Info = Color(0xFF2196F3)
}

// ============================================================================
// TYPOGRAPHY SYSTEM
// ============================================================================

/**
 * Typography scale based on Urbanist font family
 * Following Material Design 3 principles
 */
object EventTypography {
    // Font sizes (in sp)
    val DisplayLarge = 57.dp
    val DisplayMedium = 45.dp
    val DisplaySmall = 36.dp
    
    val HeadlineLarge = 32.dp
    val HeadlineMedium = 28.dp
    val HeadlineSmall = 24.dp
    
    val TitleLarge = 22.dp
    val TitleMedium = 16.dp
    val TitleSmall = 14.dp
    
    val BodyLarge = 16.dp
    val BodyMedium = 14.dp
    val BodySmall = 12.dp
    
    val LabelLarge = 14.dp
    val LabelMedium = 12.dp
    val LabelSmall = 11.dp
}

// ============================================================================
// SPACING SYSTEM
// ============================================================================

/**
 * Consistent spacing scale
 * Based on 8dp base unit
 */
object EventSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
    val xxxl = 64.dp
}

// ============================================================================
// SHAPE SYSTEM
// ============================================================================

/**
 * Corner radius system
 * Following the rounded design from Behance
 */
object EventShapes {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val full = 50.dp  // For pill-shaped buttons
}

// ============================================================================
// ELEVATION SYSTEM
// ============================================================================

/**
 * Elevation values for cards and components
 */
object EventElevation {
    val xs = 1.dp
    val sm = 2.dp
    val md = 4.dp
    val lg = 8.dp
    val xl = 16.dp
}

// ============================================================================
// THEME DEFINITION
// ============================================================================

/**
 * EventMO Design System Theme
 * Dark theme optimized for event discovery and booking
 */
@Composable
fun EventTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            // Primary colors
            primary = EventColors.Purple500,
            onPrimary = EventColors.White,
            primaryContainer = EventColors.Purple700,
            onPrimaryContainer = EventColors.White,
            
            // Secondary colors
            secondary = EventColors.Purple200,
            onSecondary = EventColors.Black,
            secondaryContainer = EventColors.Purple100,
            onSecondaryContainer = EventColors.Black,
            
            // Surface colors
            surface = EventColors.DarkGray800,
            onSurface = EventColors.White,
            surfaceVariant = EventColors.DarkGray700,
            onSurfaceVariant = EventColors.DarkGray200,
            
            // Background colors
            background = EventColors.DarkGray900,
            onBackground = EventColors.White,
            
            // Error colors
            error = EventColors.Error,
            onError = EventColors.White,
            errorContainer = Color(0xFF3E2723),
            onErrorContainer = Color(0xFFFFCDD2),
            
            // Outline colors
            outline = EventColors.DarkGray600,
            outlineVariant = EventColors.DarkGray500,
            
            // Inverse colors
            inverseSurface = EventColors.White,
            inverseOnSurface = EventColors.Black,
            inversePrimary = EventColors.Purple200,
            
            // Scrim
            scrim = EventColors.Black.copy(alpha = 0.32f),
            
            // Surface tint
            surfaceTint = EventColors.Purple500,
        )
    } else {
        lightColorScheme(
            // Primary colors
            primary = EventColors.Purple500,
            onPrimary = EventColors.White,
            primaryContainer = EventColors.Purple100,
            onPrimaryContainer = EventColors.Purple900,
            
            // Secondary colors
            secondary = EventColors.Purple700,
            onSecondary = EventColors.White,
            secondaryContainer = EventColors.Purple200,
            onSecondaryContainer = EventColors.Purple900,
            
            // Surface colors
            surface = EventColors.White,
            onSurface = EventColors.Black,
            surfaceVariant = EventColors.DarkGray100,
            onSurfaceVariant = EventColors.DarkGray700,
            
            // Background colors
            background = EventColors.White,
            onBackground = EventColors.Black,
            
            // Error colors
            error = EventColors.Error,
            onError = EventColors.White,
            errorContainer = Color(0xFFFFEBEE),
            onErrorContainer = Color(0xFFB71C1C),
            
            // Outline colors
            outline = EventColors.DarkGray300,
            outlineVariant = EventColors.DarkGray200,
            
            // Inverse colors
            inverseSurface = EventColors.Black,
            inverseOnSurface = EventColors.White,
            inversePrimary = EventColors.Purple200,
            
            // Scrim
            scrim = EventColors.Black.copy(alpha = 0.32f),
            
            // Surface tint
            surfaceTint = EventColors.Purple500,
        )
    }

    CompositionLocalProvider(
        LocalEventColors provides EventColors,
        LocalEventTypography provides EventTypography,
        LocalEventSpacing provides EventSpacing,
        LocalEventShapes provides EventShapes,
        LocalEventElevation provides EventElevation
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

// ============================================================================
// COMPOSITION LOCALS
// ============================================================================

val LocalEventColors = staticCompositionLocalOf { EventColors }
val LocalEventTypography = staticCompositionLocalOf { EventTypography }
val LocalEventSpacing = staticCompositionLocalOf { EventSpacing }
val LocalEventShapes = staticCompositionLocalOf { EventShapes }
val LocalEventElevation = staticCompositionLocalOf { EventElevation }

// ============================================================================
// THEME ACCESSORS
// ============================================================================

val MaterialTheme.eventColors: EventColors
    @Composable
    @ReadOnlyComposable
    get() = LocalEventColors.current

val MaterialTheme.eventTypography: EventTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalEventTypography.current

val MaterialTheme.eventSpacing: EventSpacing
    @Composable
    @ReadOnlyComposable
    get() = LocalEventSpacing.current

val MaterialTheme.eventShapes: EventShapes
    @Composable
    @ReadOnlyComposable
    get() = LocalEventShapes.current

val MaterialTheme.eventElevation: EventElevation
    @Composable
    @ReadOnlyComposable
    get() = LocalEventElevation.current
