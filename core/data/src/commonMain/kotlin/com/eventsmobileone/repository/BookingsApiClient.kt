package com.eventsmobileone.repository

import com.eventsmobileone.model.Booking
import com.eventsmobileone.model.BookingRequest
import com.eventsmobileone.model.BookingResponse
import com.eventsmobileone.model.PaymentIntent
import com.eventsmobileone.model.PaymentRequest
import com.eventsmobileone.model.PaymentResult
import com.eventsmobileone.model.QrCodeValidation
import com.eventsmobileone.model.QrCodeValidationRequest
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

interface BookingsApiClient {
    suspend fun createBooking(request: BookingRequest): Result<BookingResponse>
    suspend fun getBookings(userId: String): Result<List<Booking>>
    suspend fun getBookingDetails(bookingId: String): Result<Booking>
    suspend fun updateBooking(bookingId: String, request: BookingRequest): Result<Booking>
    suspend fun cancelBooking(bookingId: String): Result<Boolean>
    suspend fun confirmBooking(bookingId: String): Result<Booking>
    suspend fun getEventBookings(eventId: String): Result<List<Booking>>
    suspend fun createPaymentIntent(request: PaymentRequest): Result<PaymentIntent>
    suspend fun confirmPayment(paymentId: String): Result<PaymentResult>
    suspend fun getPaymentDetails(paymentId: String): Result<PaymentResult>
    suspend fun requestRefund(paymentId: String): Result<Boolean>
    suspend fun getPaymentHistory(userId: String): Result<List<PaymentResult>>
    suspend fun getEventQrCodes(eventId: String): Result<List<String>>
    suspend fun validateQrCode(request: QrCodeValidationRequest): Result<QrCodeValidation>
    suspend fun getBookingQrCode(bookingId: String): Result<String>
}

class BookingsApiClientImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://r2nbmrglq6.execute-api.ca-central-1.amazonaws.com/dev"
) : BookingsApiClient {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    override suspend fun createBooking(request: BookingRequest): Result<BookingResponse> {
        return try {
            val response = httpClient.post("$baseUrl/bookings") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val bookingResponse = response.body<BookingResponse>()
                    Result.success(bookingResponse)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Invalid booking request"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to create booking"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getBookings(userId: String): Result<List<Booking>> {
        return try {
            val response = httpClient.get("$baseUrl/bookings") {
                addMobileUserAgent()
                parameter("userId", userId)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val bookings = response.body<List<Booking>>()
                    Result.success(bookings)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch bookings"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getBookingDetails(bookingId: String): Result<Booking> {
        return try {
            val response = httpClient.get("$baseUrl/bookings/$bookingId") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val booking = response.body<Booking>()
                    Result.success(booking)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Booking not found"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch booking details"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun updateBooking(bookingId: String, request: BookingRequest): Result<Booking> {
        return try {
            val response = httpClient.put("$baseUrl/bookings/$bookingId") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val booking = response.body<Booking>()
                    Result.success(booking)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Invalid booking update request"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to update booking"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun cancelBooking(bookingId: String): Result<Boolean> {
        return try {
            val response = httpClient.delete("$baseUrl/bookings/$bookingId") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(true)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Booking not found"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to cancel booking"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun confirmBooking(bookingId: String): Result<Booking> {
        return try {
            val response = httpClient.post("$baseUrl/bookings/$bookingId/confirm") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val booking = response.body<Booking>()
                    Result.success(booking)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Cannot confirm booking"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to confirm booking"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getEventBookings(eventId: String): Result<List<Booking>> {
        return try {
            val response = httpClient.get("$baseUrl/bookings/event/$eventId") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val bookings = response.body<List<Booking>>()
                    Result.success(bookings)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch event bookings"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun createPaymentIntent(request: PaymentRequest): Result<PaymentIntent> {
        return try {
            val response = httpClient.post("$baseUrl/payments/create-intent") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val paymentIntent = response.body<PaymentIntent>()
                    Result.success(paymentIntent)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Invalid payment request"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to create payment intent"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun confirmPayment(paymentId: String): Result<PaymentResult> {
        return try {
            val response = httpClient.post("$baseUrl/payments/confirm") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(mapOf("paymentId" to paymentId))
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val paymentResult = response.body<PaymentResult>()
                    Result.success(paymentResult)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Payment confirmation failed"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to confirm payment"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getPaymentDetails(paymentId: String): Result<PaymentResult> {
        return try {
            val response = httpClient.get("$baseUrl/payments/$paymentId") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val paymentResult = response.body<PaymentResult>()
                    Result.success(paymentResult)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Payment not found"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch payment details"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun requestRefund(paymentId: String): Result<Boolean> {
        return try {
            val response = httpClient.post("$baseUrl/payments/$paymentId/refund") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(true)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Refund request failed"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to request refund"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getPaymentHistory(userId: String): Result<List<PaymentResult>> {
        return try {
            val response = httpClient.get("$baseUrl/payments/history") {
                addMobileUserAgent()
                parameter("userId", userId)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val paymentHistory = response.body<List<PaymentResult>>()
                    Result.success(paymentHistory)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch payment history"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getEventQrCodes(eventId: String): Result<List<String>> {
        return try {
            val response = httpClient.get("$baseUrl/qr-codes/event/$eventId") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val qrCodes = response.body<List<String>>()
                    Result.success(qrCodes)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch QR codes"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun validateQrCode(request: QrCodeValidationRequest): Result<QrCodeValidation> {
        return try {
            val response = httpClient.post("$baseUrl/qr-codes/validate") {
                contentType(ContentType.Application.Json)
                addMobileUserAgent()
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val validation = response.body<QrCodeValidation>()
                    Result.success(validation)
                }
                HttpStatusCode.BadRequest -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Invalid QR code"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to validate QR code"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
    
    override suspend fun getBookingQrCode(bookingId: String): Result<String> {
        return try {
            val response = httpClient.get("$baseUrl/qr-codes/booking/$bookingId") {
                addMobileUserAgent()
            }
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val qrCode = response.body<String>()
                    Result.success(qrCode)
                }
                HttpStatusCode.NotFound -> {
                    Result.failure(NetworkError.ClientError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "QR code not found"
                    ))
                }
                HttpStatusCode.Unauthorized -> {
                    Result.failure(AuthError.Unauthorized())
                }
                else -> {
                    Result.failure(NetworkError.ServerError(
                        statusCode = response.status.value,
                        userFriendlyMessage = "Failed to fetch QR code"
                    ))
                }
            }
        } catch (e: Exception) {
            Result.failure(NetworkError.ConnectionError(e.message ?: "Network error"))
        }
    }
}
