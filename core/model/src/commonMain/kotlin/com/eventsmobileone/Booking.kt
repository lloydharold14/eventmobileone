package com.eventsmobileone

/**
 * Booking model representing a ticket booking for an attendee
 */
data class Booking(
    val id: String,
    val eventId: String,
    val userId: String,
    val tickets: List<BookingTicket>,
    val totalAmount: Double,
    val currency: String,
    val status: BookingStatus,
    val paymentStatus: PaymentStatus,
    val attendeeInfo: AttendeeInfo,
    val bookingDate: String, // ISO 8601 format
    val eventDate: String, // ISO 8601 format
    val qrCode: String? = null,
    val bookingConfirmation: String? = null
)

/**
 * Individual ticket within a booking
 */
data class BookingTicket(
    val id: String,
    val ticketTypeId: String,
    val ticketTypeName: String,
    val price: Double,
    val currency: String,
    val attendeeName: String,
    val attendeeEmail: String? = null,
    val qrCode: String? = null,
    val isUsed: Boolean = false,
    val usedAt: String? = null // ISO 8601 format
)

/**
 * Attendee information for the booking
 */
data class AttendeeInfo(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String? = null,
    val specialRequirements: String? = null
)

/**
 * Booking status
 */
enum class BookingStatus(val displayName: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded"),
    EXPIRED("Expired")
}

/**
 * Payment status
 */
enum class PaymentStatus(val displayName: String) {
    PENDING("Pending"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    REFUNDED("Refunded"),
    CANCELLED("Cancelled")
}

/**
 * Booking request for creating a new booking
 */
data class BookingRequest(
    val eventId: String,
    val tickets: List<TicketRequest>,
    val attendeeInfo: AttendeeInfo,
    val promoCode: String? = null,
    val paymentMethod: PaymentMethod? = null
)

/**
 * Individual ticket request
 */
data class TicketRequest(
    val ticketTypeId: String,
    val quantity: Int,
    val attendeeName: String,
    val attendeeEmail: String? = null
)

/**
 * Payment method for booking
 */
data class PaymentMethod(
    val type: PaymentMethodType,
    val token: String? = null,
    val cardLast4: String? = null,
    val cardBrand: String? = null
)

/**
 * Payment method types
 */
enum class PaymentMethodType(val displayName: String) {
    CREDIT_CARD("Credit Card"),
    PAYPAL("PayPal"),
    APPLE_PAY("Apple Pay"),
    GOOGLE_PAY("Google Pay"),
    STRIPE("Stripe")
}

/**
 * Payment intent for processing payments
 */
data class PaymentIntent(
    val id: String,
    val amount: Double,
    val currency: String,
    val status: PaymentIntentStatus,
    val clientSecret: String? = null,
    val paymentMethodId: String? = null,
    val confirmationMethod: String? = null
)

/**
 * Payment intent status
 */
enum class PaymentIntentStatus(val displayName: String) {
    REQUIRES_PAYMENT_METHOD("Requires Payment Method"),
    REQUIRES_CONFIRMATION("Requires Confirmation"),
    REQUIRES_ACTION("Requires Action"),
    PROCESSING("Processing"),
    REQUIRES_CAPTURE("Requires Capture"),
    CANCELED("Canceled"),
    SUCCEEDED("Succeeded")
}

/**
 * Promo code for discounts
 */
data class PromoCode(
    val code: String,
    val discountType: DiscountType,
    val discountValue: Double,
    val currency: String? = null,
    val minimumAmount: Double? = null,
    val maximumDiscount: Double? = null,
    val validFrom: String? = null, // ISO 8601 format
    val validUntil: String? = null, // ISO 8601 format
    val usageLimit: Int? = null,
    val usageCount: Int = 0,
    val isActive: Boolean = true
)

/**
 * Discount types
 */
enum class DiscountType(val displayName: String) {
    PERCENTAGE("Percentage"),
    FIXED_AMOUNT("Fixed Amount")
}

/**
 * Booking summary for UI display
 */
data class BookingSummary(
    val bookingId: String,
    val eventTitle: String,
    val eventDate: String,
    val totalTickets: Int,
    val totalAmount: Double,
    val currency: String,
    val status: BookingStatus,
    val qrCode: String? = null
)

/**
 * Booking history for user profile
 */
data class BookingHistory(
    val bookings: List<BookingSummary>,
    val totalBookings: Int,
    val totalSpent: Double,
    val currency: String
)

