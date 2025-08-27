package com.eventsmobileone.domain.bookings

import com.eventsmobileone.model.*
import com.eventsmobileone.repository.BookingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Create a new booking for an event
 */
class CreateBookingUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(request: BookingRequest): Result<BookingResponse> = withContext(dispatcher) {
        bookingsRepository.createBooking(request)
    }
}

/**
 * Get all bookings for a user
 */
class GetUserBookingsUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String): Result<List<Booking>> = withContext(dispatcher) {
        bookingsRepository.getBookings(userId)
    }
}

/**
 * Get booking details by ID
 */
class GetBookingDetailsUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bookingId: String): Result<Booking> = withContext(dispatcher) {
        bookingsRepository.getBookingDetails(bookingId)
    }
}

/**
 * Update an existing booking
 */
class UpdateBookingUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bookingId: String, request: BookingRequest): Result<Booking> = withContext(dispatcher) {
        bookingsRepository.updateBooking(bookingId, request)
    }
}

/**
 * Cancel a booking
 */
class CancelBookingUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bookingId: String): Result<Boolean> = withContext(dispatcher) {
        bookingsRepository.cancelBooking(bookingId)
    }
}

/**
 * Confirm a booking
 */
class ConfirmBookingUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bookingId: String): Result<Booking> = withContext(dispatcher) {
        bookingsRepository.confirmBooking(bookingId)
    }
}

/**
 * Get all bookings for a specific event
 */
class GetEventBookingsUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(eventId: String): Result<List<Booking>> = withContext(dispatcher) {
        bookingsRepository.getEventBookings(eventId)
    }
}

/**
 * Create a payment intent for booking
 */
class CreatePaymentIntentUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(request: PaymentRequest): Result<PaymentIntent> = withContext(dispatcher) {
        bookingsRepository.createPaymentIntent(request)
    }
}

/**
 * Confirm a payment
 */
class ConfirmPaymentUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(paymentId: String): Result<PaymentResult> = withContext(dispatcher) {
        bookingsRepository.confirmPayment(paymentId)
    }
}

/**
 * Get payment details
 */
class GetPaymentDetailsUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(paymentId: String): Result<PaymentResult> = withContext(dispatcher) {
        bookingsRepository.getPaymentDetails(paymentId)
    }
}

/**
 * Request a refund for a payment
 */
class RequestRefundUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(paymentId: String): Result<Boolean> = withContext(dispatcher) {
        bookingsRepository.requestRefund(paymentId)
    }
}

/**
 * Get payment history for a user
 */
class GetPaymentHistoryUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: String): Result<List<PaymentResult>> = withContext(dispatcher) {
        bookingsRepository.getPaymentHistory(userId)
    }
}

/**
 * Get QR codes for an event
 */
class GetEventQrCodesUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(eventId: String): Result<List<String>> = withContext(dispatcher) {
        bookingsRepository.getEventQrCodes(eventId)
    }
}

/**
 * Validate a QR code
 */
class ValidateQrCodeUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(request: QrCodeValidationRequest): Result<QrCodeValidation> = withContext(dispatcher) {
        bookingsRepository.validateQrCode(request)
    }
}

/**
 * Get QR code for a specific booking
 */
class GetBookingQrCodeUseCase(
    private val bookingsRepository: BookingsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bookingId: String): Result<String> = withContext(dispatcher) {
        bookingsRepository.getBookingQrCode(bookingId)
    }
}
