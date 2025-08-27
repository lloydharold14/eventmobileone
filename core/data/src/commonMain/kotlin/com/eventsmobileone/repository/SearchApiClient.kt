package com.eventsmobileone.repository

import com.eventsmobileone.model.*
import com.eventsmobileone.Event
import com.eventsmobileone.NetworkError
import com.eventsmobileone.AuthError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.eventsmobileone.repository.HttpClientExtensions.addMobileUserAgent

interface SearchApiClient {
    suspend fun searchEvents(query: EventSearchQuery): Result<List<Event>>
    suspend fun getSearchSuggestions(query: String): Result<SearchSuggestions>
    suspend fun getTrendingEvents(period: String = "week"): Result<TrendingEvents>
    suspend fun getNearbyEvents(latitude: Double, longitude: Double, radius: Double = 25.0): Result<NearbyEvents>
    suspend fun getPopularEvents(limit: Int = 10): Result<List<Event>>
    suspend fun getRecommendedEvents(userId: String, limit: Int = 10): Result<List<Event>>
}

class SearchApiClientImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://r2nbmrglq6.execute-api.ca-central-1.amazonaws.com/dev"
) : SearchApiClient {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    override suspend fun searchEvents(query: EventSearchQuery): Result<List<Event>> {
        return try {
            val response = httpClient.get("$baseUrl/search/events") {
                addMobileUserAgent()
                query.let { q ->
                    q.query?.let { parameter("query", it) }
                    q.category?.let { parameter("category", it) }
                    q.location?.let { parameter("location", it) }
                    q.startDate?.let { parameter("startDate", it) }
                    q.endDate?.let { parameter("endDate", it) }
                    q.priceRange?.let { pr ->
                        pr.min?.let { parameter("minPrice", it.toString()) }
                        pr.max?.let { parameter("maxPrice", it.toString()) }
                        parameter("currency", pr.currency)
                    }
                    q.availability?.let { parameter("availability", it.name) }
                    parameter("page", q.page.toString())
                    parameter("limit", q.limit.toString())
                }
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val events = response.body<List<Event>>()
                    Result.success(events)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Invalid search parameters"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to search events"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getSearchSuggestions(query: String): Result<SearchSuggestions> {
        return try {
            val response = httpClient.get("$baseUrl/search/suggestions") {
                addMobileUserAgent()
                parameter("query", query)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val suggestions = response.body<SearchSuggestions>()
                    Result.success(suggestions)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch search suggestions"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getTrendingEvents(period: String): Result<TrendingEvents> {
        return try {
            val response = httpClient.get("$baseUrl/search/trending") {
                addMobileUserAgent()
                parameter("period", period)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val trendingEvents = response.body<TrendingEvents>()
                    Result.success(trendingEvents)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch trending events"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getNearbyEvents(latitude: Double, longitude: Double, radius: Double): Result<NearbyEvents> {
        return try {
            val response = httpClient.get("$baseUrl/search/nearby") {
                addMobileUserAgent()
                parameter("latitude", latitude.toString())
                parameter("longitude", longitude.toString())
                parameter("radius", radius.toString())
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val nearbyEvents = response.body<NearbyEvents>()
                    Result.success(nearbyEvents)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Invalid location parameters"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch nearby events"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getPopularEvents(limit: Int): Result<List<Event>> {
        return try {
            val response = httpClient.get("$baseUrl/events/popular") {
                addMobileUserAgent()
                parameter("limit", limit.toString())
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val popularEvents = response.body<List<Event>>()
                    Result.success(popularEvents)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch popular events"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getRecommendedEvents(userId: String, limit: Int): Result<List<Event>> {
        return try {
            val response = httpClient.get("$baseUrl/events/recommended") {
                addMobileUserAgent()
                parameter("userId", userId)
                parameter("limit", limit.toString())
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val recommendedEvents = response.body<List<Event>>()
                    Result.success(recommendedEvents)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch recommended events"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
}
