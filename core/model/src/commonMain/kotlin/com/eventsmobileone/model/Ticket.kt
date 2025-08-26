package com.eventsmobileone.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class Ticket(
    val id: String,
    val eventId: String,
    val eventTitle: String,
    val ticketType: TicketType,
    val price: Money,
    val quantity: Int,
    val status: TicketStatus,
    val qrCodeId: String?,
    val purchaseDate: Instant,
    val validFrom: Instant,
    val validUntil: Instant,
    val attendeeInfo: AttendeeInfo
)

@Serializable
data class TicketType(
    val id: String,
    val name: String,
    val description: String,
    val price: Money,
    val available: Int,
    val maxPerOrder: Int,
    val benefits: List<String>
)

@Serializable
data class Money(
    val amount: Double,
    val currency: String
)

@Serializable
data class AttendeeInfo(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?
)

enum class TicketStatus {
    PENDING_PAYMENT,
    CONFIRMED,
    CANCELLED,
    REFUNDED,
    USED
}

@Serializable
data class TicketPurchaseRequest(
    val eventId: String,
    val ticketTypeId: String,
    val quantity: Int,
    val attendeeInfo: AttendeeInfo
)

@Serializable
data class TicketPurchaseResponse(
    val orderId: String,
    val tickets: List<Ticket>,
    val totalAmount: Money,
    val paymentUrl: String?
)

@Serializable
data class TicketOrder(
    val id: String,
    val eventId: String,
    val eventTitle: String,
    val tickets: List<Ticket>,
    val totalAmount: Money,
    val status: OrderStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    REFUNDED
}
