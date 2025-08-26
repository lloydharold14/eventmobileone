package com.eventsmobileone.domain.tickets

import com.eventsmobileone.model.TicketPurchaseRequest
import com.eventsmobileone.model.TicketPurchaseResponse
import com.eventsmobileone.repository.TicketsRepository

class PurchaseTickets(
    private val ticketsRepository: TicketsRepository
) {
    suspend operator fun invoke(request: TicketPurchaseRequest): Result<TicketPurchaseResponse> {
        return ticketsRepository.purchaseTickets(request)
    }
}

