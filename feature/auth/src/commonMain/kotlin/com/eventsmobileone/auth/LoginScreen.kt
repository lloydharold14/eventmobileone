package com.eventsmobileone.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eventsmobileone.AuthUiEvent
import com.eventsmobileone.AuthUiState
import com.eventsmobileone.OAuthProvider
import com.eventsmobileone.OAuthUserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(state.error) {
        println("DEBUG: LoginScreen LaunchedEffect - state.error changed to: ${state.error?.userFriendlyMessage}")
        if (state.error != null) {
            println("DEBUG: LoginScreen - Error is not null, should display in UI")
        }
    }
    
    // Debug: Log all state changes
    LaunchedEffect(state) {
        println("DEBUG: LoginScreen - Full state changed. Error: ${state.error?.userFriendlyMessage}, Loading: ${state.isLoading}")
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Sign in to your account to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Enter your email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error?.userFriendlyMessage?.contains("email", ignoreCase = true) == true
        )
        
        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            visualTransformation = if (isPasswordVisible) {
                androidx.compose.ui.text.input.VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error?.userFriendlyMessage?.contains("password", ignoreCase = true) == true,
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Text(
                        text = if (isPasswordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
        
        // Error message
        state.error?.let { error ->
            println("DEBUG: Displaying error in UI: ${error.userFriendlyMessage}")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = "🚨 ERROR: ${error.userFriendlyMessage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // Debug: Show error state
        if (state.error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = "🔍 DEBUG: Error state is set: ${state.error?.userFriendlyMessage}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        
        // Forgot password link
        TextButton(
            onClick = onNavigateToForgotPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Forgot Password?")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sign in button
        Button(
            onClick = { onEvent(AuthUiEvent.SignIn(email, password)) },
            enabled = !state.isLoading && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Sign In")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        
        // OAuth buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { 
                    onEvent(AuthUiEvent.SignInWithOAuth(
                        provider = OAuthProvider.GOOGLE,
                        accessToken = "mock-google-token",
                        userData = OAuthUserData(
                            email = "user@gmail.com",
                            firstName = "Google",
                            lastName = "User",
                            picture = null
                        )
                    ))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("🔍 Continue with Google")
            }
            
            OutlinedButton(
                onClick = { 
                    onEvent(AuthUiEvent.SignInWithOAuth(
                        provider = OAuthProvider.APPLE,
                        accessToken = "mock-apple-token",
                        userData = OAuthUserData(
                            email = "user@icloud.com",
                            firstName = "Apple",
                            lastName = "User",
                            picture = null
                        )
                    ))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("🍎 Continue with Apple")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Sign up link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            TextButton(onClick = onNavigateToSignUp) {
                Text("Sign Up")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
