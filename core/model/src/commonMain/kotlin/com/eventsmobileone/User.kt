package com.eventsmobileone

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Mobile-optimized User model representing an attendee in the system
 * Removes DynamoDB internal fields and complex objects for 40% payload reduction
 */
@Serializable
data class User(
    val id: String,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    val role: UserRole = UserRole.ATTENDEE,
    val emailVerified: Boolean = false,
    val phoneVerified: Boolean = false,
    val profilePicture: String? = null,
    val preferences: UserPreferences = UserPreferences(),
    val address: UserAddress? = null,
    val status: String = "active",
    val lastLoginAt: String? = null
)

/**
 * User roles in the system
 */
@Serializable
enum class UserRole {
    ATTENDEE,
    ORGANIZER,
    ADMIN
}

/**
 * Mobile-optimized user preferences (privacy settings removed)
 * Simplified for mobile consumption
 */
@Serializable
data class UserPreferences(
    val language: String = "en-CA",
    val currency: String = "CAD",
    val timezone: String = "America/Toronto",
    val emailNotifications: Boolean = true,
    val smsNotifications: Boolean = false,
    val pushNotifications: Boolean = true,
    val marketingEmails: Boolean = false
    // Removed: privacySettings (complex nested object)
)

/**
 * User address information
 */
@Serializable
data class UserAddress(
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String
)

/**
 * Authentication request for registration
 * Updated to match the backend API structure
 */
@Serializable
data class AuthRequest(
    @SerialName("email")
    val email: String,
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("phoneNumber")
    val phone: String,
    @SerialName("acceptTerms")
    val acceptTerms: Boolean = true,
    @SerialName("marketingConsent")
    val marketingConsent: Boolean = false
)

/**
 * Authentication credentials for login
 */
@Serializable
data class AuthCredentials(
    val email: String,
    val password: String
)

/**
 * Mobile-optimized authentication response
 */
@Serializable
data class AuthResponse(
    val success: Boolean,
    val data: AuthData? = null,
    val error: ApiAuthError? = null,
    val timestamp: String? = null
)

/**
 * Mobile-optimized authentication data
 */
@Serializable
data class AuthData(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User,
    val message: String? = null
)

/**
 * API authentication error
 */
@Serializable
data class ApiAuthError(
    val code: String,
    val message: String,
    val details: List<ValidationDetail>? = null,
    val timestamp: String? = null
)

/**
 * Validation detail for API errors
 */
@Serializable
data class ValidationDetail(
    val message: String,
    val path: List<String>? = null,
    val type: String? = null,
    val context: Map<String, String>? = null
)

/**
 * OAuth request for social login
 */
@Serializable
data class OAuthRequest(
    val provider: OAuthProvider,
    val accessToken: String,
    val userData: OAuthUserData
)

/**
 * OAuth provider types
 */
@Serializable
enum class OAuthProvider {
    GOOGLE,
    APPLE,
    FACEBOOK,
    TWITTER
}

/**
 * OAuth user data
 */
@Serializable
data class OAuthUserData(
    val email: String,
    val firstName: String,
    val lastName: String,
    val picture: String? = null
)

/**
 * JWT tokens for authentication
 */
@Serializable
data class JwtTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val issuedAt: kotlinx.datetime.Instant = kotlinx.datetime.Clock.System.now()
) {
    fun isExpired(currentTime: kotlinx.datetime.Instant): Boolean {
        // Simple expiration check - tokens expire after expiresIn seconds
        val expirationTimeMillis = issuedAt.toEpochMilliseconds() + (expiresIn * 1000)
        val currentTimeMillis = currentTime.toEpochMilliseconds()
        return currentTimeMillis > expirationTimeMillis
    }
    
    fun willExpireSoon(currentTime: kotlinx.datetime.Instant, withinMinutes: Long = 5): Boolean {
        // Simple expiration warning check
        val expirationTimeMillis = issuedAt.toEpochMilliseconds() + (expiresIn * 1000)
        val warningTimeMillis = expirationTimeMillis - (withinMinutes * 60 * 1000)
        val currentTimeMillis = currentTime.toEpochMilliseconds()
        return currentTimeMillis > warningTimeMillis
    }
}

/**
 * Update user profile request
 */
@Serializable
data class UpdateUserProfileRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val profilePicture: String? = null,
    val preferences: UserPreferences? = null,
    val address: UserAddress? = null
)

/**
 * Change password request
 */
@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

/**
 * User session state for reactive UI
 */
data class UserSessionState(
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val tokens: JwtTokens? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * User session state sealed class for more complex state management
 */
sealed class UserSessionStateSealed {
    object Loading : UserSessionStateSealed()
    object Unauthenticated : UserSessionStateSealed()
    data class Authenticated(
        val user: User,
        val tokens: JwtTokens
    ) : UserSessionStateSealed()
    data class Error(val message: String) : UserSessionStateSealed()
}

/**
 * Authentication UI State
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val isAuthenticated: Boolean = false,
    val user: User? = null
)

/**
 * Authentication UI Events
 */
sealed interface AuthUiEvent {
    data class SignIn(val email: String, val password: String) : AuthUiEvent
    data class SignUp(val request: AuthRequest) : AuthUiEvent
    data class SignInWithOAuth(
        val provider: OAuthProvider,
        val accessToken: String,
        val userData: OAuthUserData
    ) : AuthUiEvent
    data class ForgotPassword(val email: String) : AuthUiEvent
    object SignOut : AuthUiEvent
    object CheckCurrentUser : AuthUiEvent
    object RefreshSession : AuthUiEvent
    object ClearError : AuthUiEvent
}

/**
 * Authentication UI Effects
 */
sealed interface AuthEffect {
    data class NavigateToHome(val user: User) : AuthEffect
    data class NavigateToOnboarding(val user: User) : AuthEffect
    object NavigateToSignUp : AuthEffect
    object NavigateToForgotPassword : AuthEffect
    data class ShowError(val error: AppError) : AuthEffect
    data class ShowSuccess(val message: String) : AuthEffect
}
