package com.eventsmobileone.repository

import com.eventsmobileone.model.NotificationPreferences
import com.eventsmobileone.model.UserNotification
import com.eventsmobileone.NetworkError
import com.eventsmobileone.AuthError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.eventsmobileone.repository.HttpClientExtensions.addMobileUserAgent

interface NotificationsApiClient {
    suspend fun getNotifications(userId: String): Result<List<UserNotification>>
    suspend fun markNotificationAsRead(notificationId: String): Result<Boolean>
    suspend fun updateNotificationPreferences(userId: String, preferences: NotificationPreferences): Result<Boolean>
    suspend fun deleteNotification(notificationId: String): Result<Boolean>
    suspend fun getNotificationPreferences(userId: String): Result<NotificationPreferences>
}

class NotificationsApiClientImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://r2nbmrglq6.execute-api.ca-central-1.amazonaws.com/dev"
) : NotificationsApiClient {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    override suspend fun getNotifications(userId: String): Result<List<UserNotification>> {
        return try {
            val response = httpClient.get("$baseUrl/notifications") {
                addMobileUserAgent()
                parameter("userId", userId)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val notifications = response.body<List<UserNotification>>()
                    Result.success(notifications)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch notifications"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun markNotificationAsRead(notificationId: String): Result<Boolean> {
        return try {
            val response = httpClient.put("$baseUrl/notifications/$notificationId/read") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(true)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Notification not found"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to mark notification as read"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun updateNotificationPreferences(userId: String, preferences: NotificationPreferences): Result<Boolean> {
        return try {
            val response = httpClient.post("$baseUrl/notifications/preferences") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(mapOf(
                    "userId" to userId,
                    "preferences" to preferences
                ))
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(true)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Invalid notification preferences"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to update notification preferences"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun deleteNotification(notificationId: String): Result<Boolean> {
        return try {
            val response = httpClient.delete("$baseUrl/notifications/$notificationId") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(true)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Notification not found"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to delete notification"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getNotificationPreferences(userId: String): Result<NotificationPreferences> {
        return try {
            val response = httpClient.get("$baseUrl/notifications/preferences") {
                addMobileUserAgent()
                parameter("userId", userId)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val preferences = response.body<NotificationPreferences>()
                    Result.success(preferences)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch notification preferences"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
}
