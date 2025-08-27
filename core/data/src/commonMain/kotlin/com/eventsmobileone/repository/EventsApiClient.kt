package com.eventsmobileone.repository

import com.eventsmobileone.Event
import com.eventsmobileone.AppError
import com.eventsmobileone.NetworkError
import com.eventsmobileone.DataError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.eventsmobileone.repository.HttpClientExtensions.addMobileUserAgent

interface EventsApiClient {
    suspend fun getEvents(
        page: Int = 1,
        limit: Int = 20,
        category: String? = null,
        location: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Int? = null
    ): Result<EventsResponse>
    
    suspend fun getEventById(eventId: String): Result<EventDetailResponse>
    suspend fun searchEvents(query: String, location: String? = null): Result<EventsResponse>
    suspend fun getCategories(): Result<CategoriesResponse>
}

data class EventsResponse(
    val success: Boolean,
    val data: EventsData? = null,
    val error: String? = null
)

data class EventsData(
    val events: List<Event>,
    val pagination: Pagination,
    val metadata: EventsMetadata? = null
)

data class EventsMetadata(
    val locale: String,
    val currency: String,
    val region: String,
    val exchangeRates: Map<String, ExchangeRate>? = null
)

data class ExchangeRate(
    val rate: Double,
    val lastUpdated: String
)

data class Pagination(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)

data class EventDetailResponse(
    val success: Boolean,
    val data: Event? = null,
    val error: String? = null
)

data class CategoriesResponse(
    val success: Boolean,
    val data: List<Category>? = null,
    val error: String? = null
)

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val color: String,
    val count: Int
)

class EventsApiClientImpl(
    private val baseUrl: String = "https://a5sma74inf.execute-api.ca-central-1.amazonaws.com/dev"
) : EventsApiClient {
    
    private val httpClient = PlatformHttpClient().client
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    override suspend fun getEvents(
        page: Int,
        limit: Int,
        category: String?,
        location: String?,
        latitude: Double?,
        longitude: Double?,
        radius: Int?
    ): Result<EventsResponse> {
        return try {
            val queryParams = mutableListOf<String>()
            queryParams.add("page=$page")
            queryParams.add("limit=$limit")
            category?.let { queryParams.add("category=$it") }
            location?.let { queryParams.add("location=$it") }
            latitude?.let { queryParams.add("latitude=$it") }
            longitude?.let { queryParams.add("longitude=$it") }
            radius?.let { queryParams.add("radius=$it") }
            
            val queryString = queryParams.joinToString("&")
            val response = httpClient.get("$baseUrl/events?$queryString") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val eventsResponse = response.body<EventsResponse>()
                    if (eventsResponse.success) {
                        Result.success(eventsResponse)
                    } else {
                        Result.failure(DataError.ParseError(
                            userFriendlyMessage = "Failed to load events. Please try again."
                        ))
                    }
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(DataError.NotFound(
                        userFriendlyMessage = "No events found for the specified criteria."
                    ))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun getEventById(eventId: String): Result<EventDetailResponse> {
        return try {
            val response = httpClient.get("$baseUrl/events/$eventId") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val eventDetailResponse = response.body<EventDetailResponse>()
                    if (eventDetailResponse.success) {
                        Result.success(eventDetailResponse)
                    } else {
                        Result.failure(DataError.ParseError(
                            userFriendlyMessage = "Failed to load event details. Please try again."
                        ))
                    }
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(DataError.NotFound(
                        userFriendlyMessage = "Event not found."
                    ))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun searchEvents(query: String, location: String?): Result<EventsResponse> {
        return try {
            val queryParams = mutableListOf<String>()
            queryParams.add("q=$query")
            location?.let { queryParams.add("location=$it") }
            
            val queryString = queryParams.joinToString("&")
            val response = httpClient.get("$baseUrl/events/search?$queryString") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val eventsResponse = response.body<EventsResponse>()
                    if (eventsResponse.success) {
                        Result.success(eventsResponse)
                    } else {
                        Result.failure(DataError.ParseError(
                            userFriendlyMessage = "Failed to search events. Please try again."
                        ))
                    }
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(DataError.NotFound(
                        userFriendlyMessage = "No events found for your search."
                    ))
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    override suspend fun getCategories(): Result<CategoriesResponse> {
        return try {
            val response = httpClient.get("$baseUrl/categories") {
                addMobileUserAgent()
                contentType(ContentType.Application.Json)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val categoriesResponse = response.body<CategoriesResponse>()
                    if (categoriesResponse.success) {
                        Result.success(categoriesResponse)
                    } else {
                        Result.failure(DataError.ParseError(
                            userFriendlyMessage = "Failed to load categories. Please try again."
                        ))
                    }
                }
                HttpStatusCode.InternalServerError -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Server error occurred. Please try again later."
                    ))
                }
                else -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Request failed. Please try again."
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(handleNetworkException(e))
        }
    }
    
    private fun handleNetworkException(e: Exception): AppError {
        return when (e) {
            is Exception -> NetworkError.ConnectionError(
                userFriendlyMessage = "Network error occurred. Please try again."
            )
            else -> NetworkError.ConnectionError(
                userFriendlyMessage = "Network error occurred. Please try again."
            )
        }
    }
}
