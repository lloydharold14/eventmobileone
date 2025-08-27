package com.eventsmobileone.domain.search

import com.eventsmobileone.model.*
import com.eventsmobileone.Event
import com.eventsmobileone.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Search events with advanced filters
 */
class SearchEventsUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: EventSearchQuery): Result<List<Event>> = withContext(dispatcher) {
        searchRepository.searchEvents(query)
    }
}

/**
 * Get search suggestions for autocomplete
 */
class GetSearchSuggestionsUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String): Result<SearchSuggestions> = withContext(dispatcher) {
        searchRepository.getSearchSuggestions(query)
    }
}

/**
 * Get trending events for a specific period
 */
class GetTrendingEventsUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(period: String = "week"): Result<TrendingEvents> = withContext(dispatcher) {
        searchRepository.getTrendingEvents(period)
    }
}

/**
 * Get nearby events based on location
 */
class GetNearbyEventsUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        latitude: Double, 
        longitude: Double, 
        radius: Double = 25.0
    ): Result<NearbyEvents> = withContext(dispatcher) {
        searchRepository.getNearbyEvents(latitude, longitude, radius)
    }
}

/**
 * Get popular events
 */
class GetPopularEventsUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<Event>> = withContext(dispatcher) {
        searchRepository.getPopularEvents(limit)
    }
}

/**
 * Get personalized event recommendations
 */
class GetRecommendedEventsUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String, limit: Int = 10): Result<List<Event>> = withContext(dispatcher) {
        searchRepository.getRecommendedEvents(userId, limit)
    }
}

/**
 * Search events by category
 */
class SearchEventsByCategoryUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(category: String, page: Int = 1, limit: Int = 20): Result<List<Event>> = withContext(dispatcher) {
        val query = EventSearchQuery(
            category = category,
            page = page,
            limit = limit
        )
        searchRepository.searchEvents(query)
    }
}

/**
 * Search events by location
 */
class SearchEventsByLocationUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(location: String, page: Int = 1, limit: Int = 20): Result<List<Event>> = withContext(dispatcher) {
        val query = EventSearchQuery(
            location = location,
            page = page,
            limit = limit
        )
        searchRepository.searchEvents(query)
    }
}

/**
 * Search events by date range
 */
class SearchEventsByDateRangeUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        startDate: String, 
        endDate: String, 
        page: Int = 1, 
        limit: Int = 20
    ): Result<List<Event>> = withContext(dispatcher) {
        val query = EventSearchQuery(
            startDate = startDate,
            endDate = endDate,
            page = page,
            limit = limit
        )
        searchRepository.searchEvents(query)
    }
}

/**
 * Search events by price range
 */
class SearchEventsByPriceRangeUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        minPrice: Double? = null,
        maxPrice: Double? = null,
        currency: String = "USD",
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Event>> = withContext(dispatcher) {
        val priceRange = PriceRange(
            min = minPrice,
            max = maxPrice,
            currency = currency
        )
        val query = EventSearchQuery(
            priceRange = priceRange,
            page = page,
            limit = limit
        )
        searchRepository.searchEvents(query)
    }
}

/**
 * Search events by availability
 */
class SearchEventsByAvailabilityUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        availability: AvailabilityFilter,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Event>> = withContext(dispatcher) {
        val query = EventSearchQuery(
            availability = availability,
            page = page,
            limit = limit
        )
        searchRepository.searchEvents(query)
    }
}

/**
 * Search events with text query
 */
class SearchEventsByTextUseCase(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Event>> = withContext(dispatcher) {
        val searchQuery = EventSearchQuery(
            query = query,
            page = page,
            limit = limit
        )
        searchRepository.searchEvents(searchQuery)
    }
}
