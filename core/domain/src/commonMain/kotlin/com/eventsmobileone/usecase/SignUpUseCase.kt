package com.eventsmobileone.usecase

import com.eventsmobileone.AuthData
import com.eventsmobileone.AuthRequest
import com.eventsmobileone.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(request: AuthRequest): Result<AuthData> = withContext(dispatcher) {
        try {
            // Validate input
            val validationResult = validateSignUpRequest(request)
            if (validationResult.isFailure) {
                return@withContext Result.failure(validationResult.exceptionOrNull() ?: Exception("Validation failed"))
            }
            
            // Attempt sign up
            val result = authRepository.signUp(request)
            
            if (result.isSuccess) {
                val authData = result.getOrThrow()
                // Session is already saved by the repository
            }
            
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun validateSignUpRequest(request: AuthRequest): Result<Unit> {
        // Email validation
        if (request.email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        
        if (!isValidEmail(request.email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        
        // Username validation
        if (request.username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }
        
        if (request.username.length < 3) {
            return Result.failure(IllegalArgumentException("Username must be at least 3 characters"))
        }
        
        if (!request.username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            return Result.failure(IllegalArgumentException("Username can only contain letters, numbers, and underscores"))
        }
        
        // Password validation
        if (request.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        
        if (request.password.length < 8) {
            return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
        }
        
        if (!request.password.any { it.isUpperCase() }) {
            return Result.failure(IllegalArgumentException("Password must contain at least one uppercase letter"))
        }
        
        if (!request.password.any { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("Password must contain at least one number"))
        }
        
        // Name validation
        if (request.firstName.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("First name cannot be empty"))
        }
        
        if (request.lastName.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("Last name cannot be empty"))
        }
        
        // Terms acceptance
        if (!request.acceptTerms) {
            return Result.failure(IllegalArgumentException("You must accept the terms and conditions"))
        }
        
        return Result.success(Unit)
    }
    
    private fun isValidEmail(email: String): Boolean {
        // Simple email validation regex that works across platforms
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }
}
