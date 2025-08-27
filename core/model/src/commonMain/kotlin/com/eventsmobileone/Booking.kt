package com.eventsmobileone.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant
import com.eventsmobileone.Event

/**
 * Mobile-optimized booking request for creating a new booking
 */
@Serializable
data class BookingRequest(
    val eventId: String,
    val ticketQuantity: Int,
    val attendeeInfo: AttendeeInfo,
    val specialRequests: String? = null,
    val paymentMethod: String? = null
)

/**
 * Mobile-optimized booking response from API
 */
@Serializable
data class BookingResponse(
    val success: Boolean,
    val message: String,
    val booking: Booking,
    val paymentIntent: PaymentIntent? = null,
    val timestamp: String? = null
)

/**
 * Mobile-optimized booking entity
 */
@Serializable
data class Booking(
    val id: String,
    val eventId: String,
    val eventTitle: String,
    val attendeeId: String,
    val status: BookingStatus,
    val ticketQuantity: Int,
    val totalAmount: Double,
    val currency: String,
    val bookingDate: String, // ISO 8601 format
    val qrCode: String? = null,
    val paymentStatus: PaymentStatus
    // Removed: complex nested objects for mobile optimization
)

/**
 * Booking status enum
 */
@Serializable
enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    REFUNDED
}

/**
 * Payment status enum
 */
@Serializable
enum class PaymentStatus {
    PENDING,
    PAID,
    FAILED,
    REFUNDED
}

/**
 * Mobile-optimized payment request for creating payment intent
 */
@Serializable
data class PaymentRequest(
    val bookingId: String,
    val amount: Double,
    val currency: String,
    val paymentMethod: String,
    val description: String? = null,
    val metadata: Map<String, String>? = null
)

/**
 * Mobile-optimized payment intent from payment processor
 */
@Serializable
data class PaymentIntent(
    val id: String,
    val clientSecret: String,
    val amount: Int, // Amount in cents
    val currency: String,
    val status: String,
    val paymentMethod: String? = null,
    val description: String? = null,
    val metadata: Map<String, String>? = null
)

/**
 * Mobile-optimized payment result
 */
@Serializable
data class PaymentResult(
    val id: String,
    val bookingId: String,
    val amount: Double,
    val currency: String,
    val status: PaymentStatus,
    val paymentMethod: String,
    val transactionId: String? = null,
    val createdAt: String, // ISO 8601 format
    val refundedAt: String? = null,
    val refundAmount: Double? = null,
    val failureReason: String? = null
)

/**
 * Mobile-optimized QR code validation request
 */
@Serializable
data class QrCodeValidationRequest(
    val qrCode: String,
    val eventId: String,
    val validationType: ValidationType = ValidationType.CHECK_IN
)

/**
 * Mobile-optimized QR code validation result
 */
@Serializable
data class QrCodeValidation(
    val isValid: Boolean,
    val bookingId: String? = null,
    val eventId: String? = null,
    val eventTitle: String? = null,
    val attendeeName: String? = null,
    val ticketQuantity: Int? = null,
    val checkInTime: String? = null, // ISO 8601 format
    val checkInLocation: String? = null,
    val validationType: ValidationType,
    val message: String,
    val errorCode: String? = null
)

/**
 * Validation type enum
 */
@Serializable
enum class ValidationType {
    CHECK_IN,
    CHECK_OUT,
    VERIFICATION
}

/**
 * Mobile-optimized notification preferences
 */
@Serializable
data class NotificationPreferences(
    val emailNotifications: Boolean = true,
    val smsNotifications: Boolean = false,
    val pushNotifications: Boolean = true,
    val marketingEmails: Boolean = false,
    val eventReminders: Boolean = true,
    val bookingConfirmations: Boolean = true,
    val paymentReceipts: Boolean = true
)

/**
 * Mobile-optimized user notification
 */
@Serializable
data class UserNotification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val createdAt: String, // ISO 8601 format
    val actionUrl: String? = null,
    val metadata: Map<String, String>? = null
)

/**
 * Notification type enum
 */
@Serializable
enum class NotificationType {
    EVENT_REMINDER,
    BOOKING_CONFIRMATION,
    PAYMENT_RECEIPT,
    EVENT_UPDATE,
    SYSTEM_MESSAGE
}

/**
 * Mobile-optimized search query for events
 */
@Serializable
data class EventSearchQuery(
    val query: String? = null,
    val category: String? = null,
    val location: String? = null,
    val startDate: String? = null, // ISO 8601 format
    val endDate: String? = null, // ISO 8601 format
    val priceRange: PriceRange? = null,
    val availability: AvailabilityFilter? = null,
    val page: Int = 1,
    val limit: Int = 20
)

/**
 * Mobile-optimized price range filter
 */
@Serializable
data class PriceRange(
    val min: Double? = null,
    val max: Double? = null,
    val currency: String = "USD"
)

/**
 * Availability filter
 */
@Serializable
enum class AvailabilityFilter {
    AVAILABLE,
    LIMITED,
    SOLD_OUT,
    ALL
}

/**
 * Mobile-optimized search suggestions
 */
@Serializable
data class SearchSuggestions(
    val events: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val trending: List<String> = emptyList()
)

/**
 * Mobile-optimized trending events
 */
@Serializable
data class TrendingEvents(
    val events: List<Event>,
    val period: String, // "today", "week", "month"
    val timestamp: String // ISO 8601 format
)

/**
 * Mobile-optimized nearby events
 */
@Serializable
data class NearbyEvents(
    val events: List<Event>,
    val location: Location,
    val radius: Double, // in kilometers
    val timestamp: String // ISO 8601 format
)

/**
 * Mobile-optimized location coordinates
 */
@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null
)

/**
 * Mobile-optimized user activity analytics
 */
@Serializable
data class UserActivity(
    val userId: String,
    val totalBookings: Int,
    val totalEvents: Int,
    val totalSpent: Double,
    val currency: String,
    val favoriteCategories: List<String>,
    val lastActivity: String, // ISO 8601 format
    val activityPeriod: String // "week", "month", "year"
)

/**
 * Mobile-optimized event analytics for attendees
 */
@Serializable
data class EventAnalytics(
    val eventId: String,
    val totalAttendees: Int,
    val totalRevenue: Double,
    val currency: String,
    val averageRating: Double? = null,
    val checkInRate: Double, // percentage
    val popularTicketTypes: List<String>,
    val peakBookingTimes: List<String>, // ISO 8601 format
    val timestamp: String // ISO 8601 format
)

