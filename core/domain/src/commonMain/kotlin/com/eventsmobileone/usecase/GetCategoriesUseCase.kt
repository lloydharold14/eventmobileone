package com.eventsmobileone.usecase

import com.eventsmobileone.EventCategory
import com.eventsmobileone.repository.EventsRepository

class GetCategoriesUseCase(
    private val eventsRepository: EventsRepository
) {
    suspend operator fun invoke(): Result<List<EventCategory>> {
        return eventsRepository.getCategories()
    }
}
