package com.eventsmobileone.repository

import com.eventsmobileone.*
import com.eventsmobileone.AppError
import com.eventsmobileone.NetworkError
import com.eventsmobileone.AuthError
import com.eventsmobileone.DataError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.statement.bodyAsText
import com.eventsmobileone.repository.HttpClientExtensions.addMobileUserAgent

interface AuthApiClient {
    suspend fun signIn(email: String, password: String): Result<AuthResponse>
    suspend fun signUp(request: AuthRequest): Result<AuthResponse>
    suspend fun signInWithOAuth(request: OAuthRequest): Result<AuthResponse>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>
    suspend fun signOut(): Result<Unit>
    suspend fun getUserProfile(): Result<User>
    suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<User>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun sendEmailVerification(email: String): Result<Unit>
}

expect class PlatformHttpClient() {
    val client: HttpClient
}

class AuthApiClientImpl(
    private val baseUrl: String = "https://r2nbmrglq6.execute-api.ca-central-1.amazonaws.com/dev"
) : AuthApiClient {
    
    private val httpClient = PlatformHttpClient().client
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    override suspend fun signIn(email: String, password: String): Result<AuthResponse> {
        return try {
            println("DEBUG: Attempting sign in for email: $email")
            println("DEBUG: Using base URL: $baseUrl")
            
            val requestBody = mapOf(
                "email" to email,
                "password" to password
            )
            println("DEBUG: Request body: $requestBody")
            
            val response = httpClient.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(requestBody)
            }
            
            // Debug User-Agent
            println("DEBUG: User-Agent being sent: EventMO Mobile App")
            
            println("DEBUG: Response status: ${response.status}")
            println("DEBUG: Response body: ${response.bodyAsText()}")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    try {
                        val authResponse = response.body<AuthResponse>()
                        println("DEBUG: Parsed auth response: $authResponse")
                        if (authResponse.success) {
                            Result.success(authResponse)
                        } else {
                            println("DEBUG: Auth response indicates failure: ${authResponse.error}")
                            Result.failure(handleAuthError(authResponse.error))
                        }
                    } catch (e: Exception) {
                        println("DEBUG: Error parsing response: ${e.message}")
                        Result.failure(NetworkError.ClientError(
                            statusCode = response.status.value,
                            userFriendlyMessage = "Failed to parse response: ${e.message}"
                        ))
                    }
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.InvalidCredentials(
                        userFriendlyMessage = "Invalid email or password. Please try again."
                    ))
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(AuthError.UserNotFound(
                        userFriendlyMessage = "User not found. Please check your email address."
                    ))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun signUp(request: AuthRequest): Result<AuthResponse> {
        return try {
            val response = httpClient.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    if (authResponse.success) {
                        Result.success(authResponse)
                    } else {
                        Result.failure(handleAuthError(authResponse.error))
                    }
                }
                HttpStatusCode.Conflict -> {
                    Result.failure(AuthError.EmailAlreadyExists(
                        userFriendlyMessage = "An account with this email already exists."
                    ))
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(AuthError.WeakPassword(
                        userFriendlyMessage = "Password is too weak. Please use a stronger password."
                    ))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun signInWithOAuth(request: OAuthRequest): Result<AuthResponse> {
        return try {
            val response = httpClient.post("$baseUrl/auth/oauth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    if (authResponse.success) {
                        Result.success(authResponse)
                    } else {
                        Result.failure(handleAuthError(authResponse.error))
                    }
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.InvalidCredentials(
                        userFriendlyMessage = "OAuth authentication failed. Please try again."
                    ))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return try {
            val response = httpClient.post("$baseUrl/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("refreshToken" to refreshToken))
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    if (authResponse.success) {
                        Result.success(authResponse)
                    } else {
                        Result.failure(handleAuthError(authResponse.error))
                    }
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.TokenExpired(
                        userFriendlyMessage = "Your session has expired. Please sign in again."
                    ))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            httpClient.post("$baseUrl/auth/logout") {
                contentType(ContentType.Application.Json)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun getUserProfile(): Result<User> {
        return try {
            val response = httpClient.get("$baseUrl/users/profile") {
                contentType(ContentType.Application.Json)
            }
            val userResponse = response.body<Map<String, Any>>()
            if (userResponse["success"] == true) {
                val userData = userResponse["data"] as Map<String, Any>
                // Convert to User object - this would need proper mapping
                Result.success(User(
                    id = userData["id"] as String,
                    email = userData["email"] as String,
                    username = userData["username"] as? String ?: "",
                    firstName = userData["firstName"] as String,
                    lastName = userData["lastName"] as String,
                    role = UserRole.ATTENDEE
                ))
            } else {
                Result.failure(GeneralError.UnknownError(
                    userFriendlyMessage = "Failed to get user profile"
                ))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<User> {
        return try {
            val response = httpClient.put("$baseUrl/users/profile") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val userResponse = response.body<Map<String, Any>>()
            if (userResponse["success"] == true) {
                val userData = userResponse["data"] as Map<String, Any>
                Result.success(User(
                    id = userData["id"] as String,
                    email = userData["email"] as String,
                    username = userData["username"] as? String ?: "",
                    firstName = userData["firstName"] as String,
                    lastName = userData["lastName"] as String,
                    role = UserRole.ATTENDEE
                ))
            } else {
                Result.failure(GeneralError.UnknownError(
                    userFriendlyMessage = "Failed to update user profile"
                ))
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            httpClient.post("$baseUrl/auth/forgot-password") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("email" to email))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun sendEmailVerification(email: String): Result<Unit> {
        return try {
            httpClient.post("$baseUrl/auth/send-verification") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("email" to email))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    private fun handleNetworkException(e: Exception): AppError {
        return when (e) {
            is Exception -> NetworkError.ConnectionError(
                userFriendlyMessage = "Network error occurred. Please try again."
            )
            else -> NetworkError.ConnectionError(
                userFriendlyMessage = "Network error occurred. Please try again."
            )
        }
    }
    
    private fun handleAuthError(error: com.eventsmobileone.ApiAuthError?): AppError {
        return when (error?.code) {
            "INVALID_CREDENTIALS" -> AuthError.InvalidCredentials(
                userFriendlyMessage = "Invalid email or password. Please try again."
            )
            "USER_NOT_FOUND" -> AuthError.UserNotFound(
                userFriendlyMessage = "User not found. Please check your email address."
            )
            "EMAIL_ALREADY_EXISTS" -> AuthError.EmailAlreadyExists(
                userFriendlyMessage = "An account with this email already exists."
            )
            "WEAK_PASSWORD" -> AuthError.WeakPassword(
                userFriendlyMessage = "Password is too weak. Please use a stronger password."
            )
            "TOKEN_EXPIRED" -> AuthError.TokenExpired(
                userFriendlyMessage = "Your session has expired. Please sign in again."
            )
            else -> AuthError.InvalidCredentials(
                userFriendlyMessage = "Authentication failed. Please try again."
            )
        }
    }
    

}
