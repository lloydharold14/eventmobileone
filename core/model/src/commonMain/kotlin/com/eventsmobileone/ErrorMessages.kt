package com.eventsmobileone

import kotlinx.serialization.Serializable

/**
 * Internationalized error messages for the Event Management Platform
 * Supports French and English languages
 */
@Serializable
data class ErrorMessages(
    val en: String,
    val fr: String
) {
    fun getMessage(language: String = "en"): String {
        return when (language.lowercase()) {
            "fr" -> fr
            else -> en
        }
    }
}

/**
 * Error message constants following the Event Management Platform error codes reference
 */
object ErrorMessageConstants {
    
    // Core Error Messages
    val VALIDATION_ERROR = ErrorMessages(
        en = "Validation error occurred. Please check your input and try again.",
        fr = "Erreur de validation. Veuillez vérifier vos informations et réessayer."
    )
    
    val UNAUTHORIZED_ACCESS = ErrorMessages(
        en = "Unauthorized access. Please sign in to continue.",
        fr = "Accès non autorisé. Veuillez vous connecter pour continuer."
    )
    
    val INVALID_CREDENTIALS = ErrorMessages(
        en = "Invalid email or password. Please try again.",
        fr = "Email ou mot de passe invalide. Veuillez réessayer."
    )
    
    val AUTHORIZATION_TOKEN_REQUIRED = ErrorMessages(
        en = "Authorization token is required. Please sign in again.",
        fr = "Un token d'autorisation est requis. Veuillez vous reconnecter."
    )
    
    val INSUFFICIENT_PERMISSIONS = ErrorMessages(
        en = "Insufficient permissions to perform this action.",
        fr = "Permissions insuffisantes pour effectuer cette action."
    )
    
    val ACCESS_FORBIDDEN = ErrorMessages(
        en = "Access forbidden. You don't have permission to access this resource.",
        fr = "Accès interdit. Vous n'avez pas la permission d'accéder à cette ressource."
    )
    
    val ADMIN_ACCESS_REQUIRED = ErrorMessages(
        en = "Admin access required for this action.",
        fr = "Accès administrateur requis pour cette action."
    )
    
    val RESOURCE_NOT_FOUND = ErrorMessages(
        en = "The requested resource was not found.",
        fr = "La ressource demandée n'a pas été trouvée."
    )
    
    val USER_NOT_FOUND = ErrorMessages(
        en = "User not found. Please check your email address.",
        fr = "Utilisateur non trouvé. Veuillez vérifier votre adresse email."
    )
    
    val EVENT_NOT_FOUND = ErrorMessages(
        en = "Event not found.",
        fr = "Événement non trouvé."
    )
    
    val BOOKING_NOT_FOUND = ErrorMessages(
        en = "Booking not found.",
        fr = "Réservation non trouvée."
    )
    
    val PAYMENT_NOT_FOUND = ErrorMessages(
        en = "Payment not found.",
        fr = "Paiement non trouvé."
    )
    
    // Conflict Error Messages
    val EMAIL_ALREADY_EXISTS = ErrorMessages(
        en = "An account with this email already exists. Please try signing in instead.",
        fr = "Un compte avec cet email existe déjà. Veuillez essayer de vous connecter à la place."
    )
    
    val EVENT_TITLE_ALREADY_EXISTS = ErrorMessages(
        en = "An event with this title already exists.",
        fr = "Un événement avec ce titre existe déjà."
    )
    
    val BOOKING_ALREADY_EXISTS = ErrorMessages(
        en = "A booking for this event already exists.",
        fr = "Une réservation pour cet événement existe déjà."
    )
    
    // Business Rule Violation Messages
    val EVENT_CAPACITY_EXCEEDED = ErrorMessages(
        en = "Event capacity exceeded. No more tickets available.",
        fr = "Capacité de l'événement dépassée. Plus de billets disponibles."
    )
    
    val EVENT_CANNOT_BE_CANCELLED = ErrorMessages(
        en = "Event cannot be cancelled after the start date.",
        fr = "L'événement ne peut pas être annulé après la date de début."
    )
    
    // Payment Error Messages
    val PAYMENT_PROCESSING_FAILED = ErrorMessages(
        en = "Payment processing failed. Please try again.",
        fr = "Le traitement du paiement a échoué. Veuillez réessayer."
    )
    
    val INVALID_PAYMENT_METHOD = ErrorMessages(
        en = "Invalid payment method. Please try a different payment option.",
        fr = "Méthode de paiement invalide. Veuillez essayer une autre option de paiement."
    )
    
    val INSUFFICIENT_FUNDS = ErrorMessages(
        en = "Insufficient funds. Please try a different payment method.",
        fr = "Fonds insuffisants. Veuillez essayer une autre méthode de paiement."
    )
    
    val CARD_DECLINED = ErrorMessages(
        en = "Card declined. Please try a different payment method.",
        fr = "Carte refusée. Veuillez essayer une autre méthode de paiement."
    )
    
    val INVALID_AMOUNT = ErrorMessages(
        en = "Invalid payment amount.",
        fr = "Montant de paiement invalide."
    )
    
    val INVALID_CURRENCY = ErrorMessages(
        en = "Invalid currency. Please try a different payment method.",
        fr = "Devise invalide. Veuillez essayer une autre méthode de paiement."
    )
    
    // QR Code Error Messages
    val QR_CODE_NOT_FOUND = ErrorMessages(
        en = "QR code not found or invalid.",
        fr = "Code QR non trouvé ou invalide."
    )
    
    val QR_CODE_EXPIRED = ErrorMessages(
        en = "QR code has expired.",
        fr = "Le code QR a expiré."
    )
    
    val QR_CODE_ALREADY_USED = ErrorMessages(
        en = "QR code has already been used.",
        fr = "Le code QR a déjà été utilisé."
    )
    
    val QR_CODE_REVOKED = ErrorMessages(
        en = "QR code has been revoked.",
        fr = "Le code QR a été révoqué."
    )
    
    val BOOKING_NOT_CONFIRMED = ErrorMessages(
        en = "Booking is not confirmed.",
        fr = "La réservation n'est pas confirmée."
    )
    
    val EVENT_NOT_ACTIVE = ErrorMessages(
        en = "Event is not active.",
        fr = "L'événement n'est pas actif."
    )
    
    val INVALID_QR_DATA = ErrorMessages(
        en = "Invalid QR code data.",
        fr = "Données du code QR invalides."
    )
    
    val VALIDATION_LIMIT_EXCEEDED = ErrorMessages(
        en = "Too many validation attempts. Please try again later.",
        fr = "Trop de tentatives de validation. Veuillez réessayer plus tard."
    )
    
    // System Error Messages
    val EXTERNAL_SERVICE_ERROR = ErrorMessages(
        en = "External service error. Please try again later.",
        fr = "Erreur du service externe. Veuillez réessayer plus tard."
    )
    
    val DATABASE_ERROR = ErrorMessages(
        en = "Database error. Please try again later.",
        fr = "Erreur de base de données. Veuillez réessayer plus tard."
    )
    
    val CONFIGURATION_ERROR = ErrorMessages(
        en = "Configuration error. Please contact support.",
        fr = "Erreur de configuration. Veuillez contacter le support."
    )
    
    val INTERNAL_SERVER_ERROR = ErrorMessages(
        en = "Internal server error. Please try again later.",
        fr = "Erreur interne du serveur. Veuillez réessayer plus tard."
    )
    
    // Network Error Messages
    val NETWORK_ERROR = ErrorMessages(
        en = "Network error occurred. Please check your connection and try again.",
        fr = "Erreur réseau. Veuillez vérifier votre connexion et réessayer."
    )
    
    val CONNECTION_TIMEOUT = ErrorMessages(
        en = "Connection timeout. Please try again.",
        fr = "Délai de connexion dépassé. Veuillez réessayer."
    )
    
    // Authentication Specific Messages
    val WEAK_PASSWORD = ErrorMessages(
        en = "Password is too weak. Please use a stronger password.",
        fr = "Le mot de passe est trop faible. Veuillez utiliser un mot de passe plus fort."
    )
    
    val TOKEN_EXPIRED = ErrorMessages(
        en = "Your session has expired. Please sign in again.",
        fr = "Votre session a expiré. Veuillez vous reconnecter."
    )
    
    val EMAIL_VERIFICATION_REQUIRED = ErrorMessages(
        en = "Email verification required. Please check your email.",
        fr = "Vérification de l'email requise. Veuillez vérifier votre email."
    )
    
    val PHONE_VERIFICATION_REQUIRED = ErrorMessages(
        en = "Phone verification required. Please check your phone.",
        fr = "Vérification du téléphone requise. Veuillez vérifier votre téléphone."
    )
    
    // Generic Messages
    val GENERIC_ERROR = ErrorMessages(
        en = "An error occurred. Please try again.",
        fr = "Une erreur s'est produite. Veuillez réessayer."
    )
    
    val TRY_AGAIN_LATER = ErrorMessages(
        en = "Please try again later.",
        fr = "Veuillez réessayer plus tard."
    )
    
    val CONTACT_SUPPORT = ErrorMessages(
        en = "Please contact support if the problem persists.",
        fr = "Veuillez contacter le support si le problème persiste."
    )
}

/**
 * Error message mapper that maps error codes to internationalized messages
 */
object ErrorMessageMapper {
    
    /**
     * Maps error codes to user-friendly messages in the specified language
     */
    fun mapErrorCodeToMessage(errorCode: String, errorMessage: String?, language: String = "en"): String {
        val baseMessage = when (errorCode) {
            "VALIDATION_ERROR" -> mapValidationError(errorMessage, language)
            "UNAUTHORIZED" -> ErrorMessageConstants.UNAUTHORIZED_ACCESS.getMessage(language)
            "FORBIDDEN" -> ErrorMessageConstants.ACCESS_FORBIDDEN.getMessage(language)
            "NOT_FOUND" -> ErrorMessageConstants.RESOURCE_NOT_FOUND.getMessage(language)
            "CONFLICT" -> mapConflictError(errorMessage, language)
            "BUSINESS_RULE_VIOLATION" -> mapBusinessRuleError(errorMessage, language)
            "PAYMENT_ERROR" -> ErrorMessageConstants.PAYMENT_PROCESSING_FAILED.getMessage(language)
            "PAYMENT_VALIDATION_ERROR" -> ErrorMessageConstants.INVALID_PAYMENT_METHOD.getMessage(language)
            "PAYMENT_NOT_FOUND" -> ErrorMessageConstants.PAYMENT_NOT_FOUND.getMessage(language)
            "PAYMENT_PROCESSING_ERROR" -> ErrorMessageConstants.PAYMENT_PROCESSING_FAILED.getMessage(language)
            "PAYMENT_GATEWAY_ERROR" -> ErrorMessageConstants.EXTERNAL_SERVICE_ERROR.getMessage(language)
            "BOOKING_ERROR" -> mapBookingError(errorMessage, language)
            "EVENT_CAPACITY_EXCEEDED" -> ErrorMessageConstants.EVENT_CAPACITY_EXCEEDED.getMessage(language)
            "QR_CODE_NOT_FOUND" -> ErrorMessageConstants.QR_CODE_NOT_FOUND.getMessage(language)
            "QR_CODE_EXPIRED" -> ErrorMessageConstants.QR_CODE_EXPIRED.getMessage(language)
            "QR_CODE_ALREADY_USED" -> ErrorMessageConstants.QR_CODE_ALREADY_USED.getMessage(language)
            "QR_CODE_REVOKED" -> ErrorMessageConstants.QR_CODE_REVOKED.getMessage(language)
            "BOOKING_NOT_CONFIRMED" -> ErrorMessageConstants.BOOKING_NOT_CONFIRMED.getMessage(language)
            "EVENT_NOT_ACTIVE" -> ErrorMessageConstants.EVENT_NOT_ACTIVE.getMessage(language)
            "INVALID_QR_DATA" -> ErrorMessageConstants.INVALID_QR_DATA.getMessage(language)
            "DECRYPTION_FAILED" -> ErrorMessageConstants.INTERNAL_SERVER_ERROR.getMessage(language)
            "VALIDATION_LIMIT_EXCEEDED" -> ErrorMessageConstants.VALIDATION_LIMIT_EXCEEDED.getMessage(language)
            "EXTERNAL_SERVICE_ERROR" -> ErrorMessageConstants.EXTERNAL_SERVICE_ERROR.getMessage(language)
            "DATABASE_ERROR" -> ErrorMessageConstants.DATABASE_ERROR.getMessage(language)
            "CONFIGURATION_ERROR" -> ErrorMessageConstants.CONFIGURATION_ERROR.getMessage(language)
            "INTERNAL_SERVER_ERROR" -> ErrorMessageConstants.INTERNAL_SERVER_ERROR.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
        }
        
        return baseMessage
    }
    
    private fun mapValidationError(errorMessage: String?, language: String): String {
        return when {
            errorMessage?.contains("email already exists", ignoreCase = true) == true -> 
                ErrorMessageConstants.EMAIL_ALREADY_EXISTS.getMessage(language)
            errorMessage?.contains("username", ignoreCase = true) == true -> 
                ErrorMessageConstants.WEAK_PASSWORD.getMessage(language)
            errorMessage?.contains("password", ignoreCase = true) == true -> 
                ErrorMessageConstants.WEAK_PASSWORD.getMessage(language)
            errorMessage?.contains("phone", ignoreCase = true) == true -> 
                ErrorMessageConstants.VALIDATION_ERROR.getMessage(language)
            errorMessage?.contains("terms", ignoreCase = true) == true -> 
                ErrorMessageConstants.VALIDATION_ERROR.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.VALIDATION_ERROR.getMessage(language)
        }
    }
    
    private fun mapConflictError(errorMessage: String?, language: String): String {
        return when {
            errorMessage?.contains("email already exists", ignoreCase = true) == true -> 
                ErrorMessageConstants.EMAIL_ALREADY_EXISTS.getMessage(language)
            errorMessage?.contains("title already exists", ignoreCase = true) == true -> 
                ErrorMessageConstants.EVENT_TITLE_ALREADY_EXISTS.getMessage(language)
            errorMessage?.contains("booking already exists", ignoreCase = true) == true -> 
                ErrorMessageConstants.BOOKING_ALREADY_EXISTS.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
        }
    }
    
    private fun mapBusinessRuleError(errorMessage: String?, language: String): String {
        return when {
            errorMessage?.contains("capacity exceeded", ignoreCase = true) == true -> 
                ErrorMessageConstants.EVENT_CAPACITY_EXCEEDED.getMessage(language)
            errorMessage?.contains("cannot be cancelled", ignoreCase = true) == true -> 
                ErrorMessageConstants.EVENT_CANNOT_BE_CANCELLED.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
        }
    }
    
    private fun mapBookingError(errorMessage: String?, language: String): String {
        return when {
            errorMessage?.contains("event is full", ignoreCase = true) == true -> 
                ErrorMessageConstants.EVENT_CAPACITY_EXCEEDED.getMessage(language)
            errorMessage?.contains("booking already exists", ignoreCase = true) == true -> 
                ErrorMessageConstants.BOOKING_ALREADY_EXISTS.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
        }
    }
}
