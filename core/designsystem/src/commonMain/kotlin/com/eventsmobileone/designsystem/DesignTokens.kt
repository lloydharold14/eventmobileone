package com.eventsmobileone.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Spacing tokens
object AppSpacing {
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}

// Typography tokens
object AppTypography {
    val caption = 12.sp
    val body = 14.sp
    val bodyLarge = 16.sp
    val title = 20.sp
    val headline = 24.sp
    val display = 32.sp
}

// Icon size tokens
object AppIconSize {
    val sm = 16.dp
    val md = 24.dp
    val lg = 32.dp
    val xl = 48.dp
}

// Corner radius tokens
object AppRadius {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
}

// Elevation tokens
object AppElevation {
    val none = 0.dp
    val sm = 2.dp
    val md = 4.dp
    val lg = 8.dp
    val xl = 16.dp
}

// Animation duration tokens
object AppAnimation {
    const val fast = 150
    const val normal = 300
    const val slow = 600
}

// Color tokens (using Material3 theme colors)
object AppColors {
    // These will be used through MaterialTheme.colorScheme
    // but we can define semantic color names here for clarity
    val primary = Color(0xFF6200EE)
    val primaryVariant = Color(0xFF3700B3)
    val secondary = Color(0xFF03DAC6)
    val background = Color(0xFFFFFFFF)
    val surface = Color(0xFFF2F2F2)
    val error = Color(0xFFB00020)
    val textPrimary = Color(0xFF000000)
    val textSecondary = Color(0xFF666666)
}

