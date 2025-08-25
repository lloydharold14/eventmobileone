package com.eventsmobileone.usecase

import com.eventsmobileone.AuthData
import com.eventsmobileone.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SignInUseCase(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthData> = withContext(dispatcher) {
        try {
            // Validate input
            if (email.isBlank() || password.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Email and password cannot be empty"))
            }
            
            if (!isValidEmail(email)) {
                return@withContext Result.failure(IllegalArgumentException("Invalid email format"))
            }
            
            if (password.length < 8) {
                return@withContext Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
            }
            
            // Attempt sign in
            val result = authRepository.signIn(email, password)
            
            if (result.isSuccess) {
                val authData = result.getOrThrow()
                // Session is already saved by the repository
            }
            
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        // Simple email validation regex that works across platforms
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }
}
