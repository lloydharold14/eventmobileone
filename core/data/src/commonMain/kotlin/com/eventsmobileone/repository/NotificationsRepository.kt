package com.eventsmobileone.repository

import com.eventsmobileone.model.NotificationPreferences
import com.eventsmobileone.model.UserNotification

interface NotificationsRepository {
    suspend fun getNotifications(userId: String): Result<List<UserNotification>>
    suspend fun markNotificationAsRead(notificationId: String): Result<Boolean>
    suspend fun updateNotificationPreferences(userId: String, preferences: NotificationPreferences): Result<Boolean>
    suspend fun deleteNotification(notificationId: String): Result<Boolean>
    suspend fun getNotificationPreferences(userId: String): Result<NotificationPreferences>
}

class NotificationsRepositoryImpl(
    private val notificationsApiClient: NotificationsApiClient
) : NotificationsRepository {
    
    override suspend fun getNotifications(userId: String): Result<List<UserNotification>> {
        return notificationsApiClient.getNotifications(userId)
    }
    
    override suspend fun markNotificationAsRead(notificationId: String): Result<Boolean> {
        return notificationsApiClient.markNotificationAsRead(notificationId)
    }
    
    override suspend fun updateNotificationPreferences(userId: String, preferences: NotificationPreferences): Result<Boolean> {
        return notificationsApiClient.updateNotificationPreferences(userId, preferences)
    }
    
    override suspend fun deleteNotification(notificationId: String): Result<Boolean> {
        return notificationsApiClient.deleteNotification(notificationId)
    }
    
    override suspend fun getNotificationPreferences(userId: String): Result<NotificationPreferences> {
        return notificationsApiClient.getNotificationPreferences(userId)
    }
}
