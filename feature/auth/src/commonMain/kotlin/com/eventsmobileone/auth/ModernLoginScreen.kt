package com.eventsmobileone.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eventsmobileone.AuthUiEvent
import com.eventsmobileone.AuthUiState
import com.eventsmobileone.OAuthProvider
import com.eventsmobileone.OAuthUserData
import com.eventsmobileone.components.*

@Composable
fun ModernLoginScreen(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    // Debug logging for state changes
    LaunchedEffect(state.error) {
        println("DEBUG: ModernLoginScreen - state.error changed to: ${state.error?.userFriendlyMessage}")
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorScheme.background,
                        colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // App Logo/Title
                Text(
                    text = "EventMO",
                    style = ModernTypography.titleLarge(),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Welcome Back!",
                    style = ModernTypography.titleMedium(),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Sign in to your account to continue",
                    style = ModernTypography.bodyMedium(),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Login Form
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Email Field
                ModernOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Enter your email",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.error?.userFriendlyMessage?.contains("email", ignoreCase = true) == true
                )
                
                // Password Field
                ModernOutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Enter your password",
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    isError = state.error?.userFriendlyMessage?.contains("password", ignoreCase = true) == true
                )
                
                // Forgot Password Link
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = onNavigateToForgotPassword) {
                                        Text(
                    text = "Forgot password?",
                    style = ModernTypography.bodyMedium(),
                    color = colorScheme.primary
                )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Login Button
            ModernPrimaryButton(
                text = "Sign In",
                onClick = {
                    onEvent(AuthUiEvent.SignIn(email, password))
                },
                enabled = email.isNotEmpty() && password.isNotEmpty() && !state.isLoading,
                isLoading = state.isLoading
            )
            
            // Error Display
            state.error?.let { error ->
                ModernErrorCard(
                    error = error.userFriendlyMessage,
                    onRetry = {
                        onEvent(AuthUiEvent.SignIn(email, password))
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Divider
            ModernDivider(text = "or")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Social Login Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Continue with",
                    style = ModernTypography.bodyMedium(),
                    textAlign = TextAlign.Center
                )
                
                // Social Login Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ModernSocialLoginButton(
                        providerName = "Google",
                        onClick = {
                            onEvent(
                                AuthUiEvent.SignInWithOAuth(
                                    provider = OAuthProvider.GOOGLE,
                                    accessToken = "mock_google_token",
                                    userData = OAuthUserData(
                                        email = "user@gmail.com",
                                        firstName = "Google",
                                        lastName = "User"
                                    )
                                )
                            )
                        }
                    )
                    
                    ModernSocialLoginButton(
                        providerName = "Apple",
                        onClick = {
                            onEvent(
                                AuthUiEvent.SignInWithOAuth(
                                    provider = OAuthProvider.APPLE,
                                    accessToken = "mock_apple_token",
                                    userData = OAuthUserData(
                                        email = "user@icloud.com",
                                        firstName = "Apple",
                                        lastName = "User"
                                    )
                                )
                            )
                        }
                    )
                    
                    ModernSocialLoginButton(
                        providerName = "Facebook",
                        onClick = {
                            // TODO: Implement Facebook OAuth
                        }
                    )
                    
                    ModernSocialLoginButton(
                        providerName = "Twitter",
                        onClick = {
                            // TODO: Implement Twitter OAuth
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sign Up Link
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "New to EventMO? ",
                    style = ModernTypography.bodyMedium()
                )
                
                TextButton(onClick = onNavigateToSignUp) {
                    Text(
                        text = "Sign up",
                        style = ModernTypography.bodyMedium().copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                        ),
                        color = colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Loading Overlay
        if (state.isLoading) {
            ModernLoadingIndicator()
        }
    }
}
