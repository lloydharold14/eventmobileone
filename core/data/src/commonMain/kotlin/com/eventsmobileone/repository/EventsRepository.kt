package com.eventsmobileone.repository

import com.eventsmobileone.Event
import com.eventsmobileone.EventCategory
import com.eventsmobileone.EventFilter

interface EventsRepository {
    suspend fun getCategories(): Result<List<EventCategory>>
    suspend fun getEvents(filter: EventFilter = EventFilter()): Result<List<Event>>
    suspend fun getEventById(id: String): Result<Event>
    suspend fun searchEvents(query: String): Result<List<Event>>
    suspend fun getEventsByCategory(categoryId: String): Result<List<Event>>
    suspend fun getFeaturedEvents(): Result<List<Event>>
}
