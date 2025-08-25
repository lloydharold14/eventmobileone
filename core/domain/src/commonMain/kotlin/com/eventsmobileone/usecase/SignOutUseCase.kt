package com.eventsmobileone.usecase

import com.eventsmobileone.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SignOutUseCase(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<Unit> = withContext(dispatcher) {
        try {
            // Clear local session
            authRepository.clearUserSession()
            
            // Call API to invalidate tokens (if available)
            authRepository.signOut()
        } catch (e: Exception) {
            // Even if API call fails, we should still clear local session
            authRepository.clearUserSession()
            Result.failure(e)
        }
    }
}
