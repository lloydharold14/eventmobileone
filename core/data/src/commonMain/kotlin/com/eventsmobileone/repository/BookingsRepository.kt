package com.eventsmobileone.repository

import com.eventsmobileone.model.*

interface BookingsRepository {
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

class BookingsRepositoryImpl(
    private val bookingsApiClient: BookingsApiClient
) : BookingsRepository {
    
    override suspend fun createBooking(request: BookingRequest): Result<BookingResponse> {
        return bookingsApiClient.createBooking(request)
    }
    
    override suspend fun getBookings(userId: String): Result<List<Booking>> {
        return bookingsApiClient.getBookings(userId)
    }
    
    override suspend fun getBookingDetails(bookingId: String): Result<Booking> {
        return bookingsApiClient.getBookingDetails(bookingId)
    }
    
    override suspend fun updateBooking(bookingId: String, request: BookingRequest): Result<Booking> {
        return bookingsApiClient.updateBooking(bookingId, request)
    }
    
    override suspend fun cancelBooking(bookingId: String): Result<Boolean> {
        return bookingsApiClient.cancelBooking(bookingId)
    }
    
    override suspend fun confirmBooking(bookingId: String): Result<Booking> {
        return bookingsApiClient.confirmBooking(bookingId)
    }
    
    override suspend fun getEventBookings(eventId: String): Result<List<Booking>> {
        return bookingsApiClient.getEventBookings(eventId)
    }
    
    override suspend fun createPaymentIntent(request: PaymentRequest): Result<PaymentIntent> {
        return bookingsApiClient.createPaymentIntent(request)
    }
    
    override suspend fun confirmPayment(paymentId: String): Result<PaymentResult> {
        return bookingsApiClient.confirmPayment(paymentId)
    }
    
    override suspend fun getPaymentDetails(paymentId: String): Result<PaymentResult> {
        return bookingsApiClient.getPaymentDetails(paymentId)
    }
    
    override suspend fun requestRefund(paymentId: String): Result<Boolean> {
        return bookingsApiClient.requestRefund(paymentId)
    }
    
    override suspend fun getPaymentHistory(userId: String): Result<List<PaymentResult>> {
        return bookingsApiClient.getPaymentHistory(userId)
    }
    
    override suspend fun getEventQrCodes(eventId: String): Result<List<String>> {
        return bookingsApiClient.getEventQrCodes(eventId)
    }
    
    override suspend fun validateQrCode(request: QrCodeValidationRequest): Result<QrCodeValidation> {
        return bookingsApiClient.validateQrCode(request)
    }
    
    override suspend fun getBookingQrCode(bookingId: String): Result<String> {
        return bookingsApiClient.getBookingQrCode(bookingId)
    }
}
