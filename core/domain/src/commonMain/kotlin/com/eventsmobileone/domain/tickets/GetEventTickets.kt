package com.eventsmobileone.domain.tickets

import com.eventsmobileone.model.TicketType
import com.eventsmobileone.repository.TicketsRepository

class GetEventTickets(
    private val ticketsRepository: TicketsRepository
) {
    suspend operator fun invoke(eventId: String): Result<List<TicketType>> {
        return ticketsRepository.getEventTickets(eventId)
    }
}

