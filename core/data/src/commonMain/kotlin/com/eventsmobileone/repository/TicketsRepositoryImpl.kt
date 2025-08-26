package com.eventsmobileone.repository

import com.eventsmobileone.model.Ticket
import com.eventsmobileone.model.TicketPurchaseRequest
import com.eventsmobileone.model.TicketPurchaseResponse
import com.eventsmobileone.model.TicketType

class TicketsRepositoryImpl(
    private val ticketsApiClient: TicketsApiClient
) : TicketsRepository {
    
    override suspend fun getEventTickets(eventId: String): Result<List<TicketType>> {
        return try {
            val tickets = ticketsApiClient.getEventTickets(eventId)
            Result.success(tickets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun purchaseTickets(request: TicketPurchaseRequest): Result<TicketPurchaseResponse> {
        return try {
            val response = ticketsApiClient.purchaseTickets(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserTickets(): Result<List<Ticket>> {
        return try {
            val tickets = ticketsApiClient.getUserTickets()
            Result.success(tickets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTicketById(ticketId: String): Result<Ticket> {
        return try {
            val ticket = ticketsApiClient.getTicketById(ticketId)
            Result.success(ticket)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

