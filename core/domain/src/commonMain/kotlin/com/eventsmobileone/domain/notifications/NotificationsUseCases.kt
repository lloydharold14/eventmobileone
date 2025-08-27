package com.eventsmobileone.domain.notifications

import com.eventsmobileone.model.NotificationPreferences
import com.eventsmobileone.model.UserNotification
import com.eventsmobileone.repository.NotificationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Get all notifications for a user
 */
class GetUserNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String): Result<List<UserNotification>> = withContext(dispatcher) {
        notificationsRepository.getNotifications(userId)
    }
}

/**
 * Mark a notification as read
 */
class MarkNotificationAsReadUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(notificationId: String): Result<Boolean> = withContext(dispatcher) {
        notificationsRepository.markNotificationAsRead(notificationId)
    }
}

/**
 * Update notification preferences
 */
class UpdateNotificationPreferencesUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String, preferences: NotificationPreferences): Result<Boolean> = withContext(dispatcher) {
        notificationsRepository.updateNotificationPreferences(userId, preferences)
    }
}

/**
 * Delete a notification
 */
class DeleteNotificationUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(notificationId: String): Result<Boolean> = withContext(dispatcher) {
        notificationsRepository.deleteNotification(notificationId)
    }
}

/**
 * Get notification preferences for a user
 */
class GetNotificationPreferencesUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String): Result<NotificationPreferences> = withContext(dispatcher) {
        notificationsRepository.getNotificationPreferences(userId)
    }
}

/**
 * Mark all notifications as read for a user
 */
class MarkAllNotificationsAsReadUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String): Result<Boolean> = withContext(dispatcher) {
        // First get all notifications
        val notificationsResult = notificationsRepository.getNotifications(userId)
        if (notificationsResult.isFailure) {
            return@withContext Result.failure(notificationsResult.exceptionOrNull() ?: Exception("Failed to get notifications"))
        }
        
        val notifications = notificationsResult.getOrNull() ?: emptyList()
        val unreadNotifications = notifications.filter { !it.isRead }
        
        // Mark each unread notification as read
        var allSuccess = true
        unreadNotifications.forEach { notification ->
            val result = notificationsRepository.markNotificationAsRead(notification.id)
            if (result.isFailure) {
                allSuccess = false
            }
        }
        
        if (allSuccess) {
            Result.success(true)
        } else {
            Result.failure(Exception("Failed to mark some notifications as read"))
        }
    }
}

/**
 * Get unread notifications count for a user
 */
class GetUnreadNotificationsCountUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String): Result<Int> = withContext(dispatcher) {
        val notificationsResult = notificationsRepository.getNotifications(userId)
        if (notificationsResult.isFailure) {
            return@withContext Result.failure(notificationsResult.exceptionOrNull() ?: Exception("Failed to get notifications"))
        }
        
        val notifications = notificationsResult.getOrNull() ?: emptyList()
        val unreadCount = notifications.count { !it.isRead }
        Result.success(unreadCount)
    }
}

/**
 * Get notifications by type for a user
 */
class GetNotificationsByTypeUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String, type: com.eventsmobileone.model.NotificationType): Result<List<UserNotification>> = withContext(dispatcher) {
        val notificationsResult = notificationsRepository.getNotifications(userId)
        if (notificationsResult.isFailure) {
            return@withContext Result.failure(notificationsResult.exceptionOrNull() ?: Exception("Failed to get notifications"))
        }
        
        val notifications = notificationsResult.getOrNull() ?: emptyList()
        val filteredNotifications = notifications.filter { it.type == type }
        Result.success(filteredNotifications)
    }
}
