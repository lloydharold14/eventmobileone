package com.eventsmobileone.repository

import com.eventsmobileone.model.Ticket
import com.eventsmobileone.model.TicketPurchaseRequest
import com.eventsmobileone.model.TicketPurchaseResponse
import com.eventsmobileone.model.TicketType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.eventsmobileone.repository.HttpClientExtensions.addMobileUserAgent

class TicketsApiClient(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val secureStorage: SecureStorage
) {
    
    suspend fun getEventTickets(eventId: String): List<TicketType> {
        val authToken = secureStorage.getAccessToken()
        val response = httpClient.get("$baseUrl/events/$eventId/tickets") {
            addMobileUserAgent()
            headers {
                append("Accept-Language", "en-CA")
                append("X-User-Currency", "CAD")
                append("X-User-Region", "CA")
                authToken?.let { append("Authorization", "Bearer $it") }
            }
        }
        return response.body<ApiResponse<List<TicketType>>>().data
    }
    
    suspend fun purchaseTickets(request: TicketPurchaseRequest): TicketPurchaseResponse {
        val authToken = secureStorage.getAccessToken()
        val response = httpClient.post("$baseUrl/events/${request.eventId}/purchase") {
            contentType(ContentType.Application.Json)
            addMobileUserAgent()
            headers {
                append("Accept-Language", "en-CA")
                append("X-User-Currency", "CAD")
                append("X-User-Region", "CA")
                authToken?.let { append("Authorization", "Bearer $it") }
            }
            setBody(request)
        }
        return response.body<ApiResponse<TicketPurchaseResponse>>().data
    }
    
    suspend fun getUserTickets(): List<Ticket> {
        val authToken = secureStorage.getAccessToken()
        val response = httpClient.get("$baseUrl/tickets") {
            addMobileUserAgent()
            headers {
                append("Accept-Language", "en-CA")
                append("X-User-Currency", "CAD")
                append("X-User-Region", "CA")
                authToken?.let { append("Authorization", "Bearer $it") }
            }
        }
        return response.body<ApiResponse<List<Ticket>>>().data
    }
    
    suspend fun getTicketById(ticketId: String): Ticket {
        val authToken = secureStorage.getAccessToken()
        val response = httpClient.get("$baseUrl/tickets/$ticketId") {
            addMobileUserAgent()
            headers {
                append("Accept-Language", "en-CA")
                append("X-User-Currency", "CAD")
                append("X-User-Region", "CA")
                authToken?.let { append("Authorization", "Bearer $it") }
            }
        }
        return response.body<ApiResponse<Ticket>>().data
    }
    
    @kotlinx.serialization.Serializable
    private data class ApiResponse<T>(
        val success: Boolean,
        val data: T,
        val timestamp: String
    )
}
