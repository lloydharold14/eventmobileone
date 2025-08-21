package com.eventsmobileone.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * EventMO Button Component
 * Following the rounded design from Behance with purple primary color
 */
@Composable
fun EventButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: EventButtonVariant = EventButtonVariant.Primary,
    size: EventButtonSize = EventButtonSize.Medium,
    fullWidth: Boolean = false
) {
    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }
    
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = buttonModifier.height(size.height),
        shape = RoundedCornerShape(50.dp), // Full rounded corners
        colors = ButtonDefaults.buttonColors(
            containerColor = variant.backgroundColor,
            contentColor = variant.contentColor,
            disabledContainerColor = variant.disabledBackgroundColor,
            disabledContentColor = variant.disabledContentColor
        ),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 16.dp
        )
    ) {
        Text(
            text = text,
            fontSize = size.textSize,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Button variants following the design system
 */
enum class EventButtonVariant(
    val backgroundColor: Color,
    val contentColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color
) {
    Primary(
        backgroundColor = Color(0xFF9C27B0), // Purple500
        contentColor = Color(0xFFFFFFFF), // White
        disabledBackgroundColor = Color(0xFF404040), // DarkGray600
        disabledContentColor = Color(0xFF8A8A8A) // DarkGray400
    ),
    
    Secondary(
        backgroundColor = Color.Transparent,
        contentColor = Color(0xFF9C27B0), // Purple500
        disabledBackgroundColor = Color.Transparent,
        disabledContentColor = Color(0xFF8A8A8A) // DarkGray400
    ),
    
    Outline(
        backgroundColor = Color.Transparent,
        contentColor = Color(0xFF9C27B0), // Purple500
        disabledBackgroundColor = Color.Transparent,
        disabledContentColor = Color(0xFF8A8A8A) // DarkGray400
    )
}

/**
 * Button sizes following the design system
 */
enum class EventButtonSize(
    val height: androidx.compose.ui.unit.Dp,
    val textSize: androidx.compose.ui.unit.TextUnit
) {
    Small(40.dp, 12.sp),
    Medium(48.dp, 14.sp),
    Large(56.dp, 16.sp)
}
