package com.eventsmobileone.repository

import com.eventsmobileone.model.Ticket
import com.eventsmobileone.model.TicketPurchaseRequest
import com.eventsmobileone.model.TicketPurchaseResponse
import com.eventsmobileone.model.TicketType

interface TicketsRepository {
    suspend fun getEventTickets(eventId: String): Result<List<TicketType>>
    suspend fun purchaseTickets(request: TicketPurchaseRequest): Result<TicketPurchaseResponse>
    suspend fun getUserTickets(): Result<List<Ticket>>
    suspend fun getTicketById(ticketId: String): Result<Ticket>
}

