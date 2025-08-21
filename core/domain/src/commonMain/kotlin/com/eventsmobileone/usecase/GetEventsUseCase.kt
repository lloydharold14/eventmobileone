package com.eventsmobileone.usecase

import com.eventsmobileone.Event
import com.eventsmobileone.EventFilter
import com.eventsmobileone.repository.EventsRepository

class GetEventsUseCase(
    private val eventsRepository: EventsRepository
) {
    suspend operator fun invoke(filter: EventFilter): Result<List<Event>> {
        return eventsRepository.getEvents(filter)
    }
}
