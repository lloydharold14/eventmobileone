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
import com.eventsmobileone.AuthRequest
import com.eventsmobileone.AuthUiEvent
import com.eventsmobileone.AuthUiState
import com.eventsmobileone.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    
    LaunchedEffect(state.error) {
        if (state.error != null) {
            // Show error snackbar or handle error display
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Join us to discover amazing events",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Name fields
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                placeholder = { Text("John") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.error?.userFriendlyMessage?.contains("first name", ignoreCase = true) == true
            )
            
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                placeholder = { Text("Doe") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.error?.userFriendlyMessage?.contains("last name", ignoreCase = true) == true
            )
        }
        
        // Username field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            placeholder = { Text("johndoe") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error?.userFriendlyMessage?.contains("username", ignoreCase = true) == true
        )
        
        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("john.doe@example.com") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error?.userFriendlyMessage?.contains("email", ignoreCase = true) == true
        )
        
        // Phone field (optional)
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone (Optional)") },
            placeholder = { Text("+1 (555) 123-4567") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Create a strong password") },
            visualTransformation = if (isPasswordVisible) {
                androidx.compose.ui.text.input.VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
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
        
        // Confirm password field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            placeholder = { Text("Confirm your password") },
            visualTransformation = if (isConfirmPasswordVisible) {
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
            isError = password.isNotBlank() && confirmPassword.isNotBlank() && password != confirmPassword,
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Text(
                        text = if (isConfirmPasswordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
        
        // Password requirements
        if (password.isNotBlank()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Password requirements:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                PasswordRequirement(
                    text = "At least 8 characters",
                    isValid = password.length >= 8
                )
                
                PasswordRequirement(
                    text = "At least one uppercase letter",
                    isValid = password.any { it.isUpperCase() }
                )
                
                PasswordRequirement(
                    text = "At least one number",
                    isValid = password.any { it.isDigit() }
                )
                
                PasswordRequirement(
                    text = "Passwords match",
                    isValid = password == confirmPassword || confirmPassword.isBlank()
                )
            }
        }
        
        // Error message
        state.error?.let { error ->
            println("DEBUG: Displaying error in SignUp UI: ${error.userFriendlyMessage}")
            Text(
                text = error.userFriendlyMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
        
        // Debug: Show error state
        if (state.error != null) {
            Text(
                text = "Error state is set: ${state.error?.userFriendlyMessage}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
        
        // Terms and conditions
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it },
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "I agree to the Terms of Service and Privacy Policy",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                if (!acceptTerms && (firstName.isNotBlank() || lastName.isNotBlank() || email.isNotBlank())) {
                    Text(
                        text = "You must accept the terms to continue",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sign up button
        Button(
            onClick = {
                println("DEBUG: Create Account button clicked")
                println("DEBUG: Email: $email, Username: $username, FirstName: $firstName, LastName: $lastName")
                println("DEBUG: Password length: ${password.length}, AcceptTerms: $acceptTerms")
                val request = AuthRequest(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    phone = phone,
                    acceptTerms = acceptTerms
                )
                println("DEBUG: Dispatching AuthUiEvent.SignUp")
                onEvent(AuthUiEvent.SignUp(request))
            },
            enabled = run {
                val isEnabled = !state.isLoading && 
                         firstName.isNotBlank() && 
                         lastName.isNotBlank() && 
                         username.isNotBlank() && 
                         email.isNotBlank() && 
                         phone.isNotBlank() &&
                         password.isNotBlank() && 
                         password == confirmPassword && 
                         password.length >= 8 && 
                         password.any { it.isUpperCase() } && 
                         password.any { it.isDigit() } && 
                         acceptTerms
                println("DEBUG: Button enabled: $isEnabled")
                println("DEBUG: Loading: ${state.isLoading}, FirstName: ${firstName.isNotBlank()}, LastName: ${lastName.isNotBlank()}, Username: ${username.isNotBlank()}")
                println("DEBUG: Email: ${email.isNotBlank()}, Phone: ${phone.isNotBlank()}, Password: ${password.isNotBlank()}, ConfirmMatch: ${password == confirmPassword}")
                println("DEBUG: PasswordLength: ${password.length >= 8}, HasUpper: ${password.any { it.isUpperCase() }}, HasDigit: ${password.any { it.isDigit() }}, AcceptTerms: $acceptTerms")
                isEnabled
            },
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
                Text("Create Account")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Sign in link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            TextButton(onClick = onNavigateToLogin) {
                Text("Sign In")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PasswordRequirement(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (isValid) "✓" else "○",
            style = MaterialTheme.typography.bodySmall,
            color = if (isValid) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (isValid) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}
