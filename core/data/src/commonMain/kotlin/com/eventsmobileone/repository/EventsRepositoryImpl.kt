package com.eventsmobileone.repository

import com.eventsmobileone.*

class EventsRepositoryImpl(
    private val eventsApiClient: EventsApiClient
) : EventsRepository {
    
    override suspend fun getEvents(filter: EventFilter): Result<List<Event>> {
        return try {
            val responseResult = eventsApiClient.getEvents(
                page = 1,
                limit = 20,
                category = filter.categoryId,
                location = filter.location
            )
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        Result.success(response.data.events)
                    } else {
                        Result.failure(Exception(response.error ?: "Failed to fetch events"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getEventById(eventId: String): Result<Event> {
        return try {
            val responseResult = eventsApiClient.getEventById(eventId)
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        Result.success(response.data)
                    } else {
                        Result.failure(Exception(response.error ?: "Failed to fetch event"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchEvents(query: String): Result<List<Event>> {
        return try {
            val responseResult = eventsApiClient.searchEvents(query, null)
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        Result.success(response.data.events)
                    } else {
                        Result.failure(Exception(response.error ?: "Failed to search events"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCategories(): Result<List<EventCategory>> {
        return try {
            val responseResult = eventsApiClient.getCategories()
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        val categories = response.data.map { category ->
                            EventCategory(
                                id = category.id,
                                name = category.name,
                                icon = category.icon,
                                color = category.color
                            )
                        }
                        Result.success(categories)
                    } else {
                        Result.failure(Exception(response.error ?: "Failed to fetch categories"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getEventsByCategory(categoryId: String): Result<List<Event>> {
        return try {
            val responseResult = eventsApiClient.getEvents(
                page = 1,
                limit = 20,
                category = categoryId
            )
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        Result.success(response.data.events)
                    } else {
                        Result.failure(Exception(response.error ?: "Failed to fetch events by category"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getFeaturedEvents(): Result<List<Event>> {
        return try {
            val responseResult = eventsApiClient.getEvents(
                page = 1,
                limit = 10
            )
            
            responseResult.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        Result.success(response.data.events)
                    } else {
                        Result.failure(Exception(response.error ?: "Failed to fetch featured events"))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
