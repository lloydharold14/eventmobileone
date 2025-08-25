package com.eventsmobileone.usecase

import com.eventsmobileone.*
import com.eventsmobileone.repository.AuthRepository

class SignInWithOAuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: OAuthRequest): Result<AuthData> {
        return authRepository.signInWithOAuth(request)
    }
}
