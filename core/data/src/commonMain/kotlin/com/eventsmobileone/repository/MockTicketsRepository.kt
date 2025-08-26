package com.eventsmobileone.repository

import com.eventsmobileone.model.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.random.Random

class MockTicketsRepository : TicketsRepository {
    
    override suspend fun getEventTickets(eventId: String): Result<List<TicketType>> {
        return Result.success(
            listOf(
                TicketType(
                    id = "early-bird",
                    name = "Early Bird",
                    description = "Early bird registration (limited)",
                    price = Money(899.99, "CAD"),
                    available = 100,
                    maxPerOrder = 5,
                    benefits = listOf("Priority seating", "Exclusive networking events", "Free workshop access")
                ),
                TicketType(
                    id = "regular",
                    name = "Regular Pass",
                    description = "Regular summit pass",
                    price = Money(1299.99, "CAD"),
                    available = 400,
                    maxPerOrder = 10,
                    benefits = listOf("Standard seating", "Networking events", "Workshop access")
                ),
                TicketType(
                    id = "premium",
                    name = "Premium Pass",
                    description = "Premium pass with exclusive networking events",
                    price = Money(1999.99, "CAD"),
                    available = 100,
                    maxPerOrder = 3,
                    benefits = listOf("VIP seating", "Exclusive networking events", "All workshops included", "Lunch included", "Meet & greet with speakers")
                )
            )
        )
    }
    
    override suspend fun purchaseTickets(request: TicketPurchaseRequest): Result<TicketPurchaseResponse> {
        val now = Clock.System.now()
        val ticketType = when (request.ticketTypeId) {
            "early-bird" -> TicketType(
                id = "early-bird",
                name = "Early Bird",
                description = "Early bird registration (limited)",
                price = Money(899.99, "CAD"),
                available = 100,
                maxPerOrder = 5,
                benefits = listOf("Priority seating", "Exclusive networking events", "Free workshop access")
            )
            "regular" -> TicketType(
                id = "regular",
                name = "Regular Pass",
                description = "Regular summit pass",
                price = Money(1299.99, "CAD"),
                available = 400,
                maxPerOrder = 10,
                benefits = listOf("Standard seating", "Networking events", "Workshop access")
            )
            "premium" -> TicketType(
                id = "premium",
                name = "Premium Pass",
                description = "Premium pass with exclusive networking events",
                price = Money(1999.99, "CAD"),
                available = 100,
                maxPerOrder = 3,
                benefits = listOf("VIP seating", "Exclusive networking events", "All workshops included", "Lunch included", "Meet & greet with speakers")
            )
            else -> throw IllegalArgumentException("Invalid ticket type")
        }
        
        val tickets = (1..request.quantity).map { index ->
            Ticket(
                id = "ticket-${Random.nextInt(100000, 999999)}-$index",
                eventId = request.eventId,
                eventTitle = "Canadian Business Leadership Summit",
                ticketType = ticketType,
                price = ticketType.price,
                quantity = 1,
                status = TicketStatus.CONFIRMED,
                qrCodeId = "qr-${Random.nextInt(100000, 999999)}-$index",
                purchaseDate = now,
                validFrom = now,
                validUntil = Instant.parse("2024-06-27T17:00:00Z"),
                attendeeInfo = request.attendeeInfo
            )
        }
        
        val totalAmount = Money(
            amount = ticketType.price.amount * request.quantity,
            currency = ticketType.price.currency
        )
        
        return Result.success(
            TicketPurchaseResponse(
                orderId = "order-${Random.nextInt(100000, 999999)}",
                tickets = tickets,
                totalAmount = totalAmount,
                paymentUrl = null // Mock implementation - no payment URL needed
            )
        )
    }
    
    override suspend fun getUserTickets(): Result<List<Ticket>> {
        val now = Clock.System.now()
        return Result.success(
            listOf(
                Ticket(
                    id = "ticket-1",
                    eventId = "event-006",
                    eventTitle = "Canadian Business Leadership Summit",
                    ticketType = TicketType(
                        id = "premium",
                        name = "Premium Pass",
                        description = "Premium pass with exclusive networking events",
                        price = Money(1999.99, "CAD"),
                        available = 100,
                        maxPerOrder = 3,
                        benefits = listOf("VIP seating", "Exclusive networking events", "All workshops included", "Lunch included", "Meet & greet with speakers")
                    ),
                    price = Money(1999.99, "CAD"),
                    quantity = 1,
                    status = TicketStatus.CONFIRMED,
                    qrCodeId = "qr-1",
                    purchaseDate = now,
                    validFrom = now,
                    validUntil = Instant.parse("2024-06-27T17:00:00Z"),
                    attendeeInfo = AttendeeInfo(
                        firstName = "John",
                        lastName = "Doe",
                        email = "john.doe@example.com",
                        phone = "+1234567890"
                    )
                ),
                Ticket(
                    id = "ticket-2",
                    eventId = "event-007",
                    eventTitle = "Tech Innovation Conference",
                    ticketType = TicketType(
                        id = "regular",
                        name = "Regular Pass",
                        description = "Regular conference pass",
                        price = Money(799.99, "CAD"),
                        available = 200,
                        maxPerOrder = 5,
                        benefits = listOf("Standard seating", "Networking events", "Workshop access")
                    ),
                    price = Money(799.99, "CAD"),
                    quantity = 1,
                    status = TicketStatus.CONFIRMED,
                    qrCodeId = "qr-2",
                    purchaseDate = now,
                    validFrom = now,
                    validUntil = Instant.parse("2024-07-15T18:00:00Z"),
                    attendeeInfo = AttendeeInfo(
                        firstName = "John",
                        lastName = "Doe",
                        email = "john.doe@example.com",
                        phone = "+1234567890"
                    )
                )
            )
        )
    }
    
    override suspend fun getTicketById(ticketId: String): Result<Ticket> {
        val now = Clock.System.now()
        return Result.success(
            Ticket(
                id = ticketId,
                eventId = "event-006",
                eventTitle = "Canadian Business Leadership Summit",
                ticketType = TicketType(
                    id = "premium",
                    name = "Premium Pass",
                    description = "Premium pass with exclusive networking events",
                    price = Money(1999.99, "CAD"),
                    available = 100,
                    maxPerOrder = 3,
                    benefits = listOf("VIP seating", "Exclusive networking events", "All workshops included", "Lunch included", "Meet & greet with speakers")
                ),
                price = Money(1999.99, "CAD"),
                quantity = 1,
                status = TicketStatus.CONFIRMED,
                qrCodeId = "qr-$ticketId",
                purchaseDate = now,
                validFrom = now,
                validUntil = Instant.parse("2024-06-27T17:00:00Z"),
                attendeeInfo = AttendeeInfo(
                    firstName = "John",
                    lastName = "Doe",
                    email = "john.doe@example.com",
                    phone = "+1234567890"
                )
            )
        )
    }
}
