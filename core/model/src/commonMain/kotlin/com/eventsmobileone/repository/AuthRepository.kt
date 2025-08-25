package com.eventsmobileone.repository

import com.eventsmobileone.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface AuthRepository {
    // Authentication operations
    suspend fun signIn(email: String, password: String): Result<AuthData>
    suspend fun signUp(request: AuthRequest): Result<AuthData>
    suspend fun signInWithOAuth(request: OAuthRequest): Result<AuthData>
    suspend fun refreshToken(refreshToken: String): Result<AuthData>
    suspend fun signOut(): Result<Unit>
    
    // User profile operations
    suspend fun getUserProfile(): Result<User>
    suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<User>
    suspend fun changePassword(request: ChangePasswordRequest): Result<Unit>
    
    // Password management
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>
    
    // Email verification
    suspend fun sendEmailVerification(email: String): Result<Unit>
    suspend fun verifyEmail(token: String): Result<Unit>
    
    // Session management
    suspend fun getCurrentUser(): User?
    suspend fun getCurrentTokens(): JwtTokens?
    suspend fun saveUserSession(user: User, tokens: JwtTokens): Result<Unit>
    suspend fun clearUserSession(): Result<Unit>
    suspend fun isUserAuthenticated(): Boolean
    suspend fun isTokenExpired(currentTime: Instant): Boolean
    suspend fun isTokenExpiringSoon(currentTime: Instant, withinMinutes: Long = 5): Boolean
    
    // Session state observation
    fun observeUserSession(): Flow<UserSessionState>
    
    // Token management
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun updateTokens(tokens: JwtTokens): Result<Unit>
}
