package com.eventsmobileone.repository

import com.eventsmobileone.*
import com.eventsmobileone.getCurrentTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Instant

class AuthRepositoryImpl(
    private val authApiClient: AuthApiClient,
    private val secureStorage: SecureStorage
) : AuthRepository {
    
    private val _userSessionState = MutableStateFlow<UserSessionState>(UserSessionState.Loading)
    
    override suspend fun signIn(email: String, password: String): Result<AuthData> {
        return try {
            val responseResult = authApiClient.signIn(email, password)
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        val authData = response.data!!
                        val tokens = JwtTokens(
                            accessToken = authData.accessToken,
                            refreshToken = authData.refreshToken,
                            expiresIn = authData.expiresIn,
                            issuedAt = getCurrentTime()
                        )
                        
                        // Save tokens securely
                        secureStorage.saveAccessToken(authData.accessToken)
                        secureStorage.saveRefreshToken(authData.refreshToken)
                        secureStorage.saveUser(authData.user)
                        
                        // Update session state
                        _userSessionState.value = UserSessionState.Authenticated(authData.user, tokens)
                        
                        Result.success(authData)
                    } else {
                        val errorMessage = response.error?.message ?: "Sign in failed"
                        _userSessionState.value = UserSessionState.Error(errorMessage)
                        Result.failure(Exception(errorMessage))
                    }
                },
                onFailure = { error ->
                    _userSessionState.value = UserSessionState.Error(error.message ?: "Sign in failed")
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            _userSessionState.value = UserSessionState.Error(e.message ?: "Sign in failed")
            Result.failure(e)
        }
    }
    
    override suspend fun signUp(request: AuthRequest): Result<AuthData> {
        return try {
            val responseResult = authApiClient.signUp(request)
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        val authData = response.data!!
                        val tokens = JwtTokens(
                            accessToken = authData.accessToken,
                            refreshToken = authData.refreshToken,
                            expiresIn = authData.expiresIn,
                            issuedAt = getCurrentTime()
                        )
                        
                        // Save tokens securely
                        secureStorage.saveAccessToken(authData.accessToken)
                        secureStorage.saveRefreshToken(authData.refreshToken)
                        secureStorage.saveUser(authData.user)
                        
                        // Update session state
                        _userSessionState.value = UserSessionState.Authenticated(authData.user, tokens)
                        
                        Result.success(authData)
                    } else {
                        val errorMessage = response.error?.message ?: "Sign up failed"
                        _userSessionState.value = UserSessionState.Error(errorMessage)
                        Result.failure(Exception(errorMessage))
                    }
                },
                onFailure = { error ->
                    _userSessionState.value = UserSessionState.Error(error.message ?: "Sign up failed")
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            _userSessionState.value = UserSessionState.Error(e.message ?: "Sign up failed")
            Result.failure(e)
        }
    }
    
    override suspend fun signInWithOAuth(request: OAuthRequest): Result<AuthData> {
        return try {
            val responseResult = authApiClient.signInWithOAuth(request)
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        val authData = response.data!!
                        val tokens = JwtTokens(
                            accessToken = authData.accessToken,
                            refreshToken = authData.refreshToken,
                            expiresIn = authData.expiresIn,
                            issuedAt = getCurrentTime()
                        )
                        
                        // Save tokens securely
                        secureStorage.saveAccessToken(authData.accessToken)
                        secureStorage.saveRefreshToken(authData.refreshToken)
                        secureStorage.saveUser(authData.user)
                        
                        // Update session state
                        _userSessionState.value = UserSessionState.Authenticated(authData.user, tokens)
                        
                        Result.success(authData)
                    } else {
                        val errorMessage = response.error?.message ?: "OAuth sign in failed"
                        _userSessionState.value = UserSessionState.Error(errorMessage)
                        Result.failure(Exception(errorMessage))
                    }
                },
                onFailure = { error ->
                    _userSessionState.value = UserSessionState.Error(error.message ?: "OAuth sign in failed")
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            _userSessionState.value = UserSessionState.Error(e.message ?: "OAuth sign in failed")
            Result.failure(e)
        }
    }
    
    override suspend fun refreshToken(refreshToken: String): Result<AuthData> {
        return try {
            val responseResult = authApiClient.refreshToken(refreshToken)
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        val authData = response.data!!
                        val tokens = JwtTokens(
                            accessToken = authData.accessToken,
                            refreshToken = authData.refreshToken,
                            expiresIn = authData.expiresIn,
                            issuedAt = getCurrentTime()
                        )
                        
                        // Save new tokens securely
                        secureStorage.saveAccessToken(authData.accessToken)
                        secureStorage.saveRefreshToken(authData.refreshToken)
                        
                        // Update session state
                        _userSessionState.value = UserSessionState.Authenticated(authData.user, tokens)
                        
                        Result.success(authData)
                    } else {
                        val errorMessage = response.error?.message ?: "Token refresh failed"
                        _userSessionState.value = UserSessionState.Error(errorMessage)
                        Result.failure(Exception(errorMessage))
                    }
                },
                onFailure = { error ->
                    _userSessionState.value = UserSessionState.Error(error.message ?: "Token refresh failed")
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            _userSessionState.value = UserSessionState.Error(e.message ?: "Token refresh failed")
            Result.failure(e)
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            // Call API to invalidate tokens
            val result = authApiClient.signOut()
            
            // Clear local storage regardless of API result
            clearUserSession()
            
            result
        } catch (e: Exception) {
            // Even if API call fails, clear local session
            clearUserSession()
            Result.failure(e)
        }
    }
    
    override suspend fun getUserProfile(): Result<User> {
        return try {
            authApiClient.getUserProfile()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<User> {
        return try {
            val result = authApiClient.updateUserProfile(request)
            if (result.isSuccess) {
                val updatedUser = result.getOrThrow()
                secureStorage.saveUser(updatedUser)
                
                // Update session state if user is currently authenticated
                val currentState = _userSessionState.value
                if (currentState is UserSessionState.Authenticated) {
                    _userSessionState.value = currentState.copy(user = updatedUser)
                }
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun changePassword(request: ChangePasswordRequest): Result<Unit> {
        // TODO: Implement password change
        return Result.failure(Exception("Not implemented"))
    }
    
    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            authApiClient.forgotPassword(email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        // TODO: Implement password reset
        return Result.failure(Exception("Not implemented"))
    }
    
    override suspend fun sendEmailVerification(email: String): Result<Unit> {
        return try {
            authApiClient.sendEmailVerification(email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun verifyEmail(token: String): Result<Unit> {
        // TODO: Implement email verification
        return Result.failure(Exception("Not implemented"))
    }
    
    override suspend fun getCurrentUser(): User? {
        return secureStorage.getUser()
    }
    
    override suspend fun getCurrentTokens(): JwtTokens? {
        val accessToken = secureStorage.getAccessToken()
        val refreshToken = secureStorage.getRefreshToken()
        
        return if (accessToken != null && refreshToken != null) {
            JwtTokens(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = 3600, // Default expiration time
                issuedAt = getCurrentTime()
            )
        } else {
            null
        }
    }
    
    override suspend fun saveUserSession(user: User, tokens: JwtTokens): Result<Unit> {
        return try {
            secureStorage.saveUser(user)
            secureStorage.saveAccessToken(tokens.accessToken)
            secureStorage.saveRefreshToken(tokens.refreshToken)
            
            _userSessionState.value = UserSessionState.Authenticated(user, tokens)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun clearUserSession(): Result<Unit> {
        return try {
            secureStorage.clearUser()
            secureStorage.clearAccessToken()
            secureStorage.clearRefreshToken()
            
            _userSessionState.value = UserSessionState.Unauthenticated
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isUserAuthenticated(): Boolean {
        val user = secureStorage.getUser()
        val accessToken = secureStorage.getAccessToken()
        return user != null && accessToken != null
    }
    
    override suspend fun isTokenExpired(currentTime: Instant): Boolean {
        val tokens = getCurrentTokens()
        return tokens?.isExpired(currentTime) ?: true
    }
    
    override suspend fun isTokenExpiringSoon(currentTime: Instant, withinMinutes: Long): Boolean {
        val tokens = getCurrentTokens()
        return tokens?.willExpireSoon(currentTime, withinMinutes) ?: true
    }
    
    override fun observeUserSession(): Flow<UserSessionState> {
        return _userSessionState.asStateFlow()
    }
    
    override suspend fun getAccessToken(): String? {
        return secureStorage.getAccessToken()
    }
    
    override suspend fun getRefreshToken(): String? {
        return secureStorage.getRefreshToken()
    }
    
    override suspend fun updateTokens(tokens: JwtTokens): Result<Unit> {
        return try {
            secureStorage.saveAccessToken(tokens.accessToken)
            secureStorage.saveRefreshToken(tokens.refreshToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Secure storage interface for platform-specific implementations
interface SecureStorage {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun clearUser()
    
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun clearAccessToken()
    
    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearRefreshToken()
}
