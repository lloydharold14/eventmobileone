package com.eventsmobileone.repository

import com.eventsmobileone.*
import com.eventsmobileone.getCurrentTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Instant

class MockAuthRepository : AuthRepository {
    private val _userSessionState = MutableStateFlow<UserSessionStateSealed>(UserSessionStateSealed.Unauthenticated)
    private var currentUser: User? = null
    private var currentTokens: JwtTokens? = null
    
    override suspend fun signIn(email: String, password: String): Result<AuthData> {
        delay(1000) // Simulate network delay
        
        return if (email == "test@example.com" && password == "TestPassword123!") {
            val user = User(
                id = "user-123",
                email = email,
                username = "testuser",
                firstName = "Test",
                lastName = "User",
                role = UserRole.ATTENDEE,
                emailVerified = true
            )
            
            val authData = AuthData(
                accessToken = "mock-access-token",
                refreshToken = "mock-refresh-token",
                expiresIn = 3600,
                user = user
            )
            
            val tokens = JwtTokens(
                accessToken = authData.accessToken,
                refreshToken = authData.refreshToken,
                expiresIn = authData.expiresIn,
                issuedAt = getCurrentTime()
            )
            
            currentUser = user
            currentTokens = tokens
            _userSessionState.value = UserSessionStateSealed.Authenticated(user, tokens)
            
            Result.success(authData)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }
    
    override suspend fun signUp(request: AuthRequest): Result<AuthData> {
        delay(1000) // Simulate network delay
        
        val user = User(
            id = "mock-user-${getCurrentTime().toEpochMilliseconds()}",
            email = request.email,
            username = request.username,
            firstName = request.firstName,
            lastName = request.lastName,
            phone = request.phone,
            role = UserRole.ATTENDEE,
            emailVerified = false
        )
        
        val authData = AuthData(
            accessToken = "mock-access-token-${getCurrentTime().toEpochMilliseconds()}",
            refreshToken = "mock-refresh-token-${getCurrentTime().toEpochMilliseconds()}",
            expiresIn = 3600,
            user = user
        )
        
        val tokens = JwtTokens(
            accessToken = authData.accessToken,
            refreshToken = authData.refreshToken,
            expiresIn = authData.expiresIn,
            issuedAt = getCurrentTime()
        )
        
        currentUser = user
        currentTokens = tokens
        _userSessionState.value = UserSessionStateSealed.Authenticated(user, tokens)
        
        return Result.success(authData)
    }
    
    override suspend fun signInWithOAuth(request: OAuthRequest): Result<AuthData> {
        delay(1000) // Simulate network delay
        
        val user = User(
            id = "oauth-user-${getCurrentTime().toEpochMilliseconds()}",
            email = request.userData.email,
            username = request.userData.email.split("@").firstOrNull() ?: "oauthuser",
            firstName = request.userData.firstName,
            lastName = request.userData.lastName,
            profilePicture = request.userData.picture,
            role = UserRole.ATTENDEE,
            emailVerified = true
        )
        
        val authData = AuthData(
            accessToken = "oauth-access-token-${getCurrentTime().toEpochMilliseconds()}",
            refreshToken = "oauth-refresh-token-${getCurrentTime().toEpochMilliseconds()}",
            expiresIn = 3600,
            user = user
        )
        
        val tokens = JwtTokens(
            accessToken = authData.accessToken,
            refreshToken = authData.refreshToken,
            expiresIn = authData.expiresIn,
            issuedAt = getCurrentTime()
        )
        
        currentUser = user
        currentTokens = tokens
        _userSessionState.value = UserSessionStateSealed.Authenticated(user, tokens)
        
        return Result.success(authData)
    }
    
    override suspend fun refreshToken(refreshToken: String): Result<AuthData> {
        delay(500) // Simulate network delay
        
        return if (currentUser != null && currentTokens != null) {
            val newTokens = JwtTokens(
                accessToken = "new-access-token-${getCurrentTime().toEpochMilliseconds()}",
                refreshToken = refreshToken,
                expiresIn = 3600,
                issuedAt = getCurrentTime()
            )
            
            currentTokens = newTokens
            _userSessionState.value = UserSessionStateSealed.Authenticated(currentUser!!, newTokens)
            
            Result.success(AuthData(
                accessToken = newTokens.accessToken,
                refreshToken = newTokens.refreshToken,
                expiresIn = newTokens.expiresIn,
                user = currentUser!!
            ))
        } else {
            Result.failure(Exception("No user session found"))
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        delay(500) // Simulate network delay
        
        currentUser = null
        currentTokens = null
        _userSessionState.value = UserSessionStateSealed.Unauthenticated
        
        return Result.success(Unit)
    }
    
    override suspend fun getUserProfile(): Result<User> {
        delay(500) // Simulate network delay
        
        return if (currentUser != null) {
            Result.success(currentUser!!)
        } else {
            Result.failure(Exception("No user session found"))
        }
    }
    
    override suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<User> {
        delay(1000) // Simulate network delay
        
        return if (currentUser != null) {
            val updatedUser = currentUser!!.copy(
                firstName = request.firstName ?: currentUser!!.firstName,
                lastName = request.lastName ?: currentUser!!.lastName,
                phone = request.phone ?: currentUser!!.phone,
                profilePicture = request.profilePicture ?: currentUser!!.profilePicture,
                preferences = request.preferences ?: currentUser!!.preferences
            )
            
            currentUser = updatedUser
            if (currentTokens != null) {
                _userSessionState.value = UserSessionStateSealed.Authenticated(updatedUser, currentTokens!!)
            }
            
            Result.success(updatedUser)
        } else {
            Result.failure(Exception("No user session found"))
        }
    }
    
    override suspend fun changePassword(request: ChangePasswordRequest): Result<Unit> {
        delay(1000) // Simulate network delay
        return Result.success(Unit)
    }
    
    override suspend fun forgotPassword(email: String): Result<Unit> {
        delay(1000) // Simulate network delay
        return Result.success(Unit)
    }
    
    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        delay(1000) // Simulate network delay
        return Result.success(Unit)
    }
    
    override suspend fun sendEmailVerification(email: String): Result<Unit> {
        delay(1000) // Simulate network delay
        return Result.success(Unit)
    }
    
    override suspend fun verifyEmail(token: String): Result<Unit> {
        delay(1000) // Simulate network delay
        return Result.success(Unit)
    }
    
    override suspend fun getCurrentUser(): User? {
        return currentUser
    }
    
    override suspend fun getCurrentTokens(): JwtTokens? {
        return currentTokens
    }
    
    override suspend fun saveUserSession(user: User, tokens: JwtTokens): Result<Unit> {
        currentUser = user
        currentTokens = tokens
                    _userSessionState.value = UserSessionStateSealed.Authenticated(user, tokens)
        return Result.success(Unit)
    }
    
    override suspend fun clearUserSession(): Result<Unit> {
        currentUser = null
        currentTokens = null
        _userSessionState.value = UserSessionStateSealed.Unauthenticated
        return Result.success(Unit)
    }
    
    override suspend fun isUserAuthenticated(): Boolean {
        return currentUser != null && currentTokens != null
    }
    
    override suspend fun isTokenExpired(currentTime: Instant): Boolean {
        return currentTokens?.isExpired(currentTime) ?: true
    }
    
    override suspend fun isTokenExpiringSoon(currentTime: Instant, withinMinutes: Long): Boolean {
        return currentTokens?.willExpireSoon(currentTime, withinMinutes) ?: true
    }
    
    override fun observeUserSession(): Flow<UserSessionStateSealed> {
        return _userSessionState.asStateFlow()
    }
    
    override suspend fun getAccessToken(): String? {
        return currentTokens?.accessToken
    }
    
    override suspend fun getRefreshToken(): String? {
        return currentTokens?.refreshToken
    }
    
    override suspend fun updateTokens(tokens: JwtTokens): Result<Unit> {
        currentTokens = tokens
        if (currentUser != null) {
            _userSessionState.value = UserSessionStateSealed.Authenticated(currentUser!!, tokens)
        }
        return Result.success(Unit)
    }
}
