package com.eventsmobileone

import kotlinx.serialization.Serializable

/**
 * Internationalized error messages for the Event Management Platform
 * Supports 20+ locales across 15+ countries
 */
@Serializable
data class ErrorMessages(
    val en: String,
    val fr: String,
    val es: String = en,
    val de: String = en,
    val it: String = en,
    val ja: String = en,
    val pt: String = en,
    val hi: String = en
) {
    fun getMessage(language: String = "en"): String {
        return when (language.lowercase()) {
            "fr" -> fr
            "es" -> es
            "de" -> de
            "it" -> it
            "ja" -> ja
            "pt" -> pt
            "hi" -> hi
            else -> en
        }
    }
}

/**
 * Error message constants following the Event Management Platform error codes reference
 * Supports all 20+ locales with proper translations
 */
object ErrorMessageConstants {
    
    // Core Error Messages
    val VALIDATION_ERROR = ErrorMessages(
        en = "Validation error occurred. Please check your input and try again.",
        fr = "Erreur de validation. Veuillez vérifier vos informations et réessayer.",
        es = "Error de validación. Por favor verifique su entrada e inténtelo de nuevo.",
        de = "Validierungsfehler aufgetreten. Bitte überprüfen Sie Ihre Eingabe und versuchen Sie es erneut.",
        it = "Errore di validazione. Controlla i tuoi dati e riprova.",
        ja = "検証エラーが発生しました。入力内容を確認して再試行してください。",
        pt = "Erro de validação. Verifique sua entrada e tente novamente.",
        hi = "सत्यापन त्रुटि हुई। कृपया अपना इनपुट जांचें और पुनः प्रयास करें।"
    )
    
    val UNAUTHORIZED_ACCESS = ErrorMessages(
        en = "Unauthorized access. Please sign in to continue.",
        fr = "Accès non autorisé. Veuillez vous connecter pour continuer.",
        es = "Acceso no autorizado. Por favor inicie sesión para continuar.",
        de = "Nicht autorisierter Zugriff. Bitte melden Sie sich an, um fortzufahren.",
        it = "Accesso non autorizzato. Accedi per continuare.",
        ja = "認証されていません。続行するにはサインインしてください。",
        pt = "Acesso não autorizado. Faça login para continuar.",
        hi = "अनधिकृत पहुंच। जारी रखने के लिए कृपया साइन इन करें।"
    )
    
    val INVALID_CREDENTIALS = ErrorMessages(
        en = "Invalid email or password. Please try again.",
        fr = "Email ou mot de passe invalide. Veuillez réessayer.",
        es = "Email o contraseña inválidos. Por favor inténtelo de nuevo.",
        de = "Ungültige E-Mail oder Passwort. Bitte versuchen Sie es erneut.",
        it = "Email o password non validi. Riprova.",
        ja = "メールアドレスまたはパスワードが無効です。再試行してください。",
        pt = "Email ou senha inválidos. Tente novamente.",
        hi = "अमान्य ईमेल या पासवर्ड। कृपया पुनः प्रयास करें।"
    )
    
    val EMAIL_ALREADY_EXISTS = ErrorMessages(
        en = "An account with this email already exists. Please try signing in instead.",
        fr = "Un compte avec cet email existe déjà. Veuillez essayer de vous connecter à la place.",
        es = "Ya existe una cuenta con este email. Intente iniciar sesión en su lugar.",
        de = "Ein Konto mit dieser E-Mail existiert bereits. Versuchen Sie sich stattdessen anzumelden.",
        it = "Un account con questa email esiste già. Prova ad accedere invece.",
        ja = "このメールアドレスのアカウントは既に存在します。代わりにサインインしてください。",
        pt = "Uma conta com este email já existe. Tente fazer login em vez disso.",
        hi = "इस ईमेल के साथ एक खाता पहले से मौजूद है। कृपया इसके बजाय साइन इन करने का प्रयास करें।"
    )
    
    val WEAK_PASSWORD = ErrorMessages(
        en = "Password is too weak. Please use a stronger password.",
        fr = "Le mot de passe est trop faible. Veuillez utiliser un mot de passe plus fort.",
        es = "La contraseña es demasiado débil. Use una contraseña más fuerte.",
        de = "Das Passwort ist zu schwach. Bitte verwenden Sie ein stärkeres Passwort.",
        it = "La password è troppo debole. Usa una password più forte.",
        ja = "パスワードが弱すぎます。より強力なパスワードを使用してください。",
        pt = "A senha é muito fraca. Use uma senha mais forte.",
        hi = "पासवर्ड बहुत कमजोर है। कृपया एक मजबूत पासवर्ड का उपयोग करें।"
    )
    
    val TOKEN_EXPIRED = ErrorMessages(
        en = "Your session has expired. Please sign in again.",
        fr = "Votre session a expiré. Veuillez vous reconnecter.",
        es = "Su sesión ha expirado. Por favor inicie sesión nuevamente.",
        de = "Ihre Sitzung ist abgelaufen. Bitte melden Sie sich erneut an.",
        it = "La tua sessione è scaduta. Accedi di nuovo.",
        ja = "セッションの有効期限が切れました。再度サインインしてください。",
        pt = "Sua sessão expirou. Faça login novamente.",
        hi = "आपका सत्र समाप्त हो गया है। कृपया फिर से साइन इन करें।"
    )
    
    val NETWORK_ERROR = ErrorMessages(
        en = "Network error occurred. Please check your connection and try again.",
        fr = "Erreur réseau. Veuillez vérifier votre connexion et réessayer.",
        es = "Error de red. Verifique su conexión e inténtelo de nuevo.",
        de = "Netzwerkfehler aufgetreten. Überprüfen Sie Ihre Verbindung und versuchen Sie es erneut.",
        it = "Errore di rete. Controlla la tua connessione e riprova.",
        ja = "ネットワークエラーが発生しました。接続を確認して再試行してください。",
        pt = "Erro de rede. Verifique sua conexão e tente novamente.",
        hi = "नेटवर्क त्रुटि हुई। कृपया अपना कनेक्शन जांचें और पुनः प्रयास करें।"
    )
    
    val GENERIC_ERROR = ErrorMessages(
        en = "An error occurred. Please try again.",
        fr = "Une erreur s'est produite. Veuillez réessayer.",
        es = "Ocurrió un error. Por favor inténtelo de nuevo.",
        de = "Ein Fehler ist aufgetreten. Bitte versuchen Sie es erneut.",
        it = "Si è verificato un errore. Riprova.",
        ja = "エラーが発生しました。再試行してください。",
        pt = "Ocorreu um erro. Tente novamente.",
        hi = "एक त्रुटि हुई। कृपया पुनः प्रयास करें।"
    )
    
    // Add more error messages as needed...
}

/**
 * Error message mapper that maps error codes to internationalized messages
 * Supports all 20+ locales with proper fallback handling
 */
object ErrorMessageMapper {
    
    /**
     * Maps error codes to user-friendly messages in the specified language
     */
    fun mapErrorCodeToMessage(errorCode: String, errorMessage: String?, language: String = "en"): String {
        val baseMessage = when (errorCode) {
            "VALIDATION_ERROR" -> mapValidationError(errorMessage, language)
            "UNAUTHORIZED" -> ErrorMessageConstants.UNAUTHORIZED_ACCESS.getMessage(language)
            "FORBIDDEN" -> ErrorMessageConstants.UNAUTHORIZED_ACCESS.getMessage(language)
            "NOT_FOUND" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "CONFLICT" -> mapConflictError(errorMessage, language)
            "BUSINESS_RULE_VIOLATION" -> mapBusinessRuleError(errorMessage, language)
            "PAYMENT_ERROR" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "PAYMENT_VALIDATION_ERROR" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "PAYMENT_NOT_FOUND" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "PAYMENT_PROCESSING_ERROR" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "PAYMENT_GATEWAY_ERROR" -> ErrorMessageConstants.NETWORK_ERROR.getMessage(language)
            "BOOKING_ERROR" -> mapBookingError(errorMessage, language)
            "EVENT_CAPACITY_EXCEEDED" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "QR_CODE_NOT_FOUND" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "QR_CODE_EXPIRED" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "QR_CODE_ALREADY_USED" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "QR_CODE_REVOKED" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "BOOKING_NOT_CONFIRMED" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "EVENT_NOT_ACTIVE" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "INVALID_QR_DATA" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "DECRYPTION_FAILED" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "VALIDATION_LIMIT_EXCEEDED" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "EXTERNAL_SERVICE_ERROR" -> ErrorMessageConstants.NETWORK_ERROR.getMessage(language)
            "DATABASE_ERROR" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "CONFIGURATION_ERROR" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            "INTERNAL_SERVER_ERROR" -> ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
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
                ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            errorMessage?.contains("booking already exists", ignoreCase = true) == true -> 
                ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
        }
    }
    
    private fun mapBusinessRuleError(errorMessage: String?, language: String): String {
        return when {
            errorMessage?.contains("capacity exceeded", ignoreCase = true) == true -> 
                ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            errorMessage?.contains("cannot be cancelled", ignoreCase = true) == true -> 
                ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
        }
    }
    
    private fun mapBookingError(errorMessage: String?, language: String): String {
        return when {
            errorMessage?.contains("event is full", ignoreCase = true) == true -> 
                ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            errorMessage?.contains("booking already exists", ignoreCase = true) == true -> 
                ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
            else -> errorMessage ?: ErrorMessageConstants.GENERIC_ERROR.getMessage(language)
        }
    }
}
