package com.eventsmobileone

/**
 * Sealed class representing all possible application errors
 */
sealed class AppError : Throwable() {
    abstract val userFriendlyMessage: String
    
    override val message: String
        get() = userFriendlyMessage
}

/**
 * Network-related errors
 */
sealed class NetworkError : AppError() {
    data class ConnectionError(
        override val userFriendlyMessage: String = "No internet connection. Please check your network and try again."
    ) : NetworkError() {
        override val message: String = "Connection error"
    }
    
    data class TimeoutError(
        override val userFriendlyMessage: String = "Request timed out. Please try again."
    ) : NetworkError() {
        override val message: String = "Request timeout"
    }
    
    data class ServerError(
        val statusCode: Int,
        override val userFriendlyMessage: String = "Server error occurred. Please try again later."
    ) : NetworkError() {
        override val message: String = "Server error (HTTP $statusCode)"
    }
    
    data class ClientError(
        val statusCode: Int,
        override val userFriendlyMessage: String = "Invalid request. Please check your input and try again."
    ) : NetworkError() {
        override val message: String = "Client error (HTTP $statusCode)"
    }
}

/**
 * Authentication-related errors
 */
sealed class AuthError : AppError() {
    data class InvalidCredentials(
        override val userFriendlyMessage: String = "Invalid email or password. Please try again."
    ) : AuthError() {
        override val message: String = "Invalid credentials"
    }
    
    data class UserNotFound(
        override val userFriendlyMessage: String = "User not found. Please check your email address."
    ) : AuthError() {
        override val message: String = "User not found"
    }
    
    data class EmailAlreadyExists(
        override val userFriendlyMessage: String = "An account with this email already exists."
    ) : AuthError() {
        override val message: String = "Email already exists"
    }
    
    data class WeakPassword(
        override val userFriendlyMessage: String = "Password is too weak. Please use a stronger password."
    ) : AuthError() {
        override val message: String = "Weak password"
    }
    
    data class TokenExpired(
        override val userFriendlyMessage: String = "Your session has expired. Please sign in again."
    ) : AuthError() {
        override val message: String = "Token expired"
    }
    
    data class Unauthorized(
        override val userFriendlyMessage: String = "You are not authorized to perform this action."
    ) : AuthError() {
        override val message: String = "Unauthorized"
    }
}

/**
 * Data-related errors
 */
sealed class DataError : AppError() {
    data class ParseError(
        override val userFriendlyMessage: String = "Failed to process data. Please try again."
    ) : DataError() {
        override val message: String = "Parse error"
    }
    
    data class NotFound(
        override val userFriendlyMessage: String = "The requested data was not found."
    ) : DataError() {
        override val message: String = "Data not found"
    }
    
    data class ValidationError(
        val field: String? = null,
        override val userFriendlyMessage: String = "Invalid data provided. Please check your input."
    ) : DataError() {
        override val message: String = "Validation error${field?.let { " in $it" } ?: ""}"
    }
}

/**
 * Location-related errors
 */
sealed class LocationError : AppError() {
    data class PermissionDenied(
        override val userFriendlyMessage: String = "Location permission is required to find events near you."
    ) : LocationError() {
        override val message: String = "Location permission denied"
    }
    
    data class LocationUnavailable(
        override val userFriendlyMessage: String = "Unable to get your location. Please try again."
    ) : LocationError() {
        override val message: String = "Location unavailable"
    }
    
    data class GeocodingError(
        override val userFriendlyMessage: String = "Unable to find the specified location."
    ) : LocationError() {
        override val message: String = "Geocoding error"
    }
}

/**
 * General application errors
 */
sealed class GeneralError : AppError() {
    data class UnknownError(
        override val userFriendlyMessage: String = "An unexpected error occurred. Please try again."
    ) : GeneralError() {
        override val message: String = "Unknown error"
    }
    
    data class FeatureUnavailable(
        override val userFriendlyMessage: String = "This feature is currently unavailable."
    ) : GeneralError() {
        override val message: String = "Feature unavailable"
    }
}
