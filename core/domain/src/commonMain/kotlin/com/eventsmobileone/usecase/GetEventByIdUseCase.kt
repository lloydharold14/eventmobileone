package com.eventsmobileone.usecase

import com.eventsmobileone.Event
import com.eventsmobileone.repository.EventsRepository

class GetEventByIdUseCase(
    private val eventsRepository: EventsRepository
) {
    suspend operator fun invoke(id: String): Result<Event> {
        return eventsRepository.getEventById(id)
    }
}
