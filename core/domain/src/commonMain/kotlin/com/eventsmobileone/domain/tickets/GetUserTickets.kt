package com.eventsmobileone.domain.tickets

import com.eventsmobileone.model.Ticket
import com.eventsmobileone.repository.TicketsRepository

class GetUserTickets(
    private val ticketsRepository: TicketsRepository
) {
    suspend operator fun invoke(): Result<List<Ticket>> {
        return ticketsRepository.getUserTickets()
    }
}

