package com.eventsmobileone.usecase

import com.eventsmobileone.User
import com.eventsmobileone.repository.AuthRepository
import com.eventsmobileone.getCurrentTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): User? = withContext(dispatcher) {
        try {
            // Check if user is authenticated
            if (!authRepository.isUserAuthenticated()) {
                return@withContext null
            }
            
            // Check if token is expired
            val currentTime = getCurrentTime()
            if (authRepository.isTokenExpired(currentTime)) {
                // Try to refresh token
                val refreshToken = authRepository.getRefreshToken()
                if (refreshToken != null) {
                    val refreshResult = authRepository.refreshToken(refreshToken)
                    if (refreshResult.isFailure) {
                        // Refresh failed, clear session
                        authRepository.clearUserSession()
                        return@withContext null
                    }
                } else {
                    // No refresh token, clear session
                    authRepository.clearUserSession()
                    return@withContext null
                }
            }
            
            // Get current user
            authRepository.getCurrentUser()
        } catch (e: Exception) {
            // On any error, clear session and return null
            authRepository.clearUserSession()
            null
        }
    }
}
