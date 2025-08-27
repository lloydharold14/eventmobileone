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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eventsmobileone.AuthUiEvent
import com.eventsmobileone.AuthUiState
import com.eventsmobileone.AuthRequest
import com.eventsmobileone.components.*

@Composable
fun ModernSignUpScreen(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    
    // Debug logging for state changes
    LaunchedEffect(state.error) {
        println("DEBUG: ModernSignUpScreen - state.error changed to: ${state.error?.userFriendlyMessage}")
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
            Spacer(modifier = Modifier.height(32.dp))
            
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
                    text = "Create Account",
                    style = ModernTypography.titleMedium(),
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Join us and discover amazing events",
                    style = ModernTypography.bodyMedium(),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sign Up Form
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name Fields Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModernOutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = "First Name",
                        placeholder = "Enter first name",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isError = state.error?.userFriendlyMessage?.contains("first", ignoreCase = true) == true,
                        modifier = Modifier.weight(1f)
                    )
                    
                    ModernOutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = "Last Name",
                        placeholder = "Enter last name",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isError = state.error?.userFriendlyMessage?.contains("last", ignoreCase = true) == true,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Username Field
                ModernOutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    placeholder = "Enter username",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.error?.userFriendlyMessage?.contains("username", ignoreCase = true) == true
                )
                
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
                
                // Phone Field
                ModernOutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    placeholder = "Enter your phone number",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.error?.userFriendlyMessage?.contains("phone", ignoreCase = true) == true
                )
                
                // Password Field
                ModernOutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Create a password",
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.error?.userFriendlyMessage?.contains("password", ignoreCase = true) == true
                )
                
                // Confirm Password Field
                ModernOutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    placeholder = "Confirm your password",
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    isError = state.error?.userFriendlyMessage?.contains("confirm", ignoreCase = true) == true
                )
                
                // Terms and Conditions
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Checkbox(
                        checked = acceptTerms,
                        onCheckedChange = { acceptTerms = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorScheme.primary,
                            uncheckedColor = colorScheme.outline
                        )
                    )
                    
                    Text(
                        text = "I agree to the Terms of Service and Privacy Policy",
                        style = ModernTypography.bodyMedium(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Create Account Button
            ModernPrimaryButton(
                text = "Create Account",
                onClick = {
                    onEvent(
                        AuthUiEvent.SignUp(
                            request = AuthRequest(
                                email = email,
                                password = password,
                                firstName = firstName,
                                lastName = lastName,
                                username = username,
                                phone = phone,
                                acceptTerms = acceptTerms
                            )
                        )
                    )
                },
                enabled = firstName.isNotEmpty() && 
                         lastName.isNotEmpty() && 
                         username.isNotEmpty() && 
                         email.isNotEmpty() && 
                         phone.isNotEmpty() &&
                         password.isNotEmpty() && 
                         confirmPassword.isNotEmpty() && 
                         acceptTerms && 
                         !state.isLoading,
                isLoading = state.isLoading
            )
            
            // Error Display
            state.error?.let { error ->
                ModernErrorCard(
                    error = error.userFriendlyMessage,
                    onRetry = {
                        onEvent(
                            AuthUiEvent.SignUp(
                                request = AuthRequest(
                                    email = email,
                                    password = password,
                                    firstName = firstName,
                                    lastName = lastName,
                                    username = username,
                                    phone = phone,
                                    acceptTerms = acceptTerms
                                )
                            )
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sign In Link
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    style = ModernTypography.bodyMedium()
                )
                
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Sign in",
                        style = ModernTypography.bodyMedium().copy(
                            fontWeight = FontWeight.SemiBold
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
