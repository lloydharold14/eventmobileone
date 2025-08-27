package com.eventsmobileone.repository

import com.eventsmobileone.model.*
import com.eventsmobileone.Event

interface SearchRepository {
    suspend fun searchEvents(query: EventSearchQuery): Result<List<Event>>
    suspend fun getSearchSuggestions(query: String): Result<SearchSuggestions>
    suspend fun getTrendingEvents(period: String): Result<TrendingEvents>
    suspend fun getNearbyEvents(latitude: Double, longitude: Double, radius: Double): Result<NearbyEvents>
    suspend fun getPopularEvents(limit: Int): Result<List<Event>>
    suspend fun getRecommendedEvents(userId: String, limit: Int): Result<List<Event>>
}

class SearchRepositoryImpl(
    private val searchApiClient: SearchApiClient
) : SearchRepository {
    
    override suspend fun searchEvents(query: EventSearchQuery): Result<List<Event>> {
        return searchApiClient.searchEvents(query)
    }
    
    override suspend fun getSearchSuggestions(query: String): Result<SearchSuggestions> {
        return searchApiClient.getSearchSuggestions(query)
    }
    
    override suspend fun getTrendingEvents(period: String): Result<TrendingEvents> {
        return searchApiClient.getTrendingEvents(period)
    }
    
    override suspend fun getNearbyEvents(latitude: Double, longitude: Double, radius: Double): Result<NearbyEvents> {
        return searchApiClient.getNearbyEvents(latitude, longitude, radius)
    }
    
    override suspend fun getPopularEvents(limit: Int): Result<List<Event>> {
        return searchApiClient.getPopularEvents(limit)
    }
    
    override suspend fun getRecommendedEvents(userId: String, limit: Int): Result<List<Event>> {
        return searchApiClient.getRecommendedEvents(userId, limit)
    }
}
