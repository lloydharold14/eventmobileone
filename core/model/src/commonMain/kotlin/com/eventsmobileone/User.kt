package com.eventsmobileone

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

// User authentication models
@Serializable
data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    val role: UserRole = UserRole.ATTENDEE,
    val emailVerified: Boolean = false,
    val profilePicture: String? = null,
    val preferences: UserPreferences? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
) {
    val fullName: String get() = "$firstName $lastName"
    val displayName: String get() = if (firstName.isNotBlank()) fullName else email
}

@Serializable
enum class UserRole {
    ATTENDEE,
    ORGANIZER,
    ADMIN
}

@Serializable
data class UserPreferences(
    val notifications: Boolean = true,
    val newsletter: Boolean = false,
    val locationSharing: Boolean = false,
    val socialFeatures: Boolean = true
)

// Authentication models
@Serializable
data class AuthCredentials(
    val email: String,
    val password: String
)

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val role: UserRole = UserRole.ATTENDEE,
    val acceptTerms: Boolean = true
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val data: AuthData? = null,
    val error: ApiAuthError? = null
)

@Serializable
data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User
)

@Serializable
data class ApiAuthError(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)

// OAuth models
@Serializable
data class OAuthRequest(
    val provider: OAuthProvider,
    val accessToken: String,
    val userData: OAuthUserData
)

@Serializable
enum class OAuthProvider {
    GOOGLE,
    FACEBOOK,
    APPLE
}

@Serializable
data class OAuthUserData(
    val email: String,
    val firstName: String,
    val lastName: String,
    val picture: String? = null
)

// JWT Token management
@Serializable
data class JwtTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val issuedAt: Instant
) {
    fun isExpired(currentTime: Instant): Boolean {
        // Simple expiration check - tokens expire after expiresIn seconds
        val expirationTimeMillis = issuedAt.toEpochMilliseconds() + (expiresIn * 1000)
        val currentTimeMillis = currentTime.toEpochMilliseconds()
        return currentTimeMillis > expirationTimeMillis
    }
    
    fun willExpireSoon(currentTime: Instant, withinMinutes: Long = 5): Boolean {
        // Simple expiration warning check
        val expirationTimeMillis = issuedAt.toEpochMilliseconds() + (expiresIn * 1000)
        val warningTimeMillis = expirationTimeMillis - (withinMinutes * 60 * 1000)
        val currentTimeMillis = currentTime.toEpochMilliseconds()
        return currentTimeMillis > warningTimeMillis
    }
}

// User session state
sealed class UserSessionState {
    object Loading : UserSessionState()
    object Unauthenticated : UserSessionState()
    data class Authenticated(
        val user: User,
        val tokens: JwtTokens
    ) : UserSessionState()
    data class Error(val message: String) : UserSessionState()
}

// Authentication UI state
@Serializable
data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: AppError? = null,
    val isAuthenticated: Boolean = false,
    val isOnboardingComplete: Boolean = false
)

// Authentication events
sealed interface AuthUiEvent {
    data class SignIn(val email: String, val password: String) : AuthUiEvent
    data class SignUp(val request: AuthRequest) : AuthUiEvent
    data class SignInWithOAuth(val provider: OAuthProvider, val accessToken: String, val userData: OAuthUserData) : AuthUiEvent
    data class ForgotPassword(val email: String) : AuthUiEvent
    data object SignOut : AuthUiEvent
    data object RefreshSession : AuthUiEvent
    data object ClearError : AuthUiEvent
}

// Authentication effects
sealed interface AuthEffect {
    data class NavigateToHome(val user: User) : AuthEffect
    data class NavigateToOnboarding(val user: User) : AuthEffect
    data class ShowError(val error: AppError) : AuthEffect
    data class ShowSuccess(val message: String) : AuthEffect
    data object NavigateToForgotPassword : AuthEffect
    data object NavigateToSignUp : AuthEffect
}

// User profile update
@Serializable
data class UpdateUserProfileRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val profilePicture: String? = null,
    val preferences: UserPreferences? = null
)

// Password change
@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

// Email verification
@Serializable
data class EmailVerificationRequest(
    val email: String
)

@Serializable
data class EmailVerificationResponse(
    val success: Boolean,
    val message: String
)
