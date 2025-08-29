package com.eventsmobileone.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.ArrowBack
// import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eventsmobileone.components.ModernTypography
import com.eventsmobileone.components.ModernPrimaryButton
import com.eventsmobileone.components.ModernOutlinedTextField
import com.eventsmobileone.AuthUiState
import com.eventsmobileone.AuthUiEvent

@Composable
fun ForgotPasswordScreen(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isEmailSent by remember { mutableStateOf(false) }
    
    LaunchedEffect(state) {
        if (state.error == null && state.isLoading == false && isEmailSent) {
            // Show success message
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                // TODO: Add platform-specific icon implementation
                Text("←", style = ModernTypography.titleLarge())
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Forgot Password",
                style = ModernTypography.titleMedium(),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Description
        Text(
            text = "Enter your email address and we'll send you a link to reset your password.",
            style = ModernTypography.bodyLarge(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (!isEmailSent) {
            // Email Input
            ModernOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "Enter your email",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                isError = state.error != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Error Message
            state.error?.let { error ->
                Text(
                    text = error.userFriendlyMessage,
                    style = ModernTypography.bodyMedium(),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Send Reset Email Button
            ModernPrimaryButton(
                text = "Send Reset Email",
                onClick = {
                    if (email.trim().isNotEmpty()) {
                        onEvent(AuthUiEvent.ForgotPassword(email.trim()))
                        isEmailSent = true
                    }
                },
                enabled = email.trim().isNotEmpty() && !state.isLoading,
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Back to Login
            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Back to Login",
                    style = ModernTypography.labelMedium(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            // Success State
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO: Add platform-specific icon implementation
                    Text(
                        text = "📧",
                        style = ModernTypography.titleLarge(),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Check Your Email",
                        style = ModernTypography.titleMedium(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "We've sent a password reset link to:\n$email",
                        style = ModernTypography.bodyMedium(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    ModernPrimaryButton(
                        text = "Back to Login",
                        onClick = onNavigateToLogin,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    TextButton(
                        onClick = {
                            isEmailSent = false
                            email = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Send to Different Email",
                            style = ModernTypography.labelMedium(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
