package com.eventsmobileone.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eventsmobileone.AppError

@Composable
fun ErrorDisplay(
    error: AppError,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Error icon
        Text(
            text = "⚠️",
            style = MaterialTheme.typography.displayMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Error message
        Text(
            text = error.userFriendlyMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // Retry button if provided
        onRetry?.let { retryAction ->
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = retryAction,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Try Again")
            }
        }
    }
}

@Composable
fun ErrorSnackbar(
    error: AppError,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    // This is a placeholder for Snackbar implementation
    // In a real app, you would use SnackbarHostState
    Text(error.userFriendlyMessage)
}

@Composable
fun LoadingErrorDisplay(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Error icon
        Text(
            text = "📡",
            style = MaterialTheme.typography.displayMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Error title
        Text(
            text = "Connection Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Error message
        Text(
            text = error.userFriendlyMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Retry button
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retry")
        }
    }
}

@Composable
fun EmptyStateDisplay(
    title: String,
    message: String,
    icon: String = "📭",
    onAction: (() -> Unit)? = null,
    actionText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty state icon
        Text(
            text = icon,
            style = MaterialTheme.typography.displayMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Empty state title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Empty state message
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Action button if provided
        onAction?.let { action ->
            actionText?.let { text ->
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = action,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text)
                }
            }
        }
    }
}
