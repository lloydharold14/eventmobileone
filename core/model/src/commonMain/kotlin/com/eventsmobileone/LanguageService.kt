package com.eventsmobileone

/**
 * Language service interface for managing app language and locale
 * Supports 20+ locales across 15+ countries as provided by the backend
 */
interface LanguageService {
    /**
     * Get the current language code (e.g., "en", "fr")
     */
    fun getCurrentLanguage(): String
    
    /**
     * Get the current locale code (e.g., "en-US", "fr-CA")
     */
    fun getCurrentLocale(): String
    
    /**
     * Set the current language code
     */
    suspend fun setLanguage(languageCode: String)
    
    /**
     * Set the current locale code
     */
    suspend fun setLocale(localeCode: String)
    
    /**
     * Get supported languages
     */
    fun getSupportedLanguages(): List<Language>
    
    /**
     * Get supported locales
     */
    fun getSupportedLocales(): List<Locale>
    
    /**
     * Get current currency code
     */
    fun getCurrentCurrency(): String
    
    /**
     * Set current currency code
     */
    suspend fun setCurrency(currencyCode: String)
    
    /**
     * Get supported currencies
     */
    fun getSupportedCurrencies(): List<Currency>
    
    /**
     * Get Accept-Language header value for API calls
     */
    fun getAcceptLanguageHeader(): String
    
    /**
     * Detect locale from system preferences
     */
    suspend fun detectSystemLocale(): String
}

/**
 * Language data class
 */
data class Language(
    val code: String,
    val name: String,
    val nativeName: String
)

/**
 * Locale data class with currency support
 */
data class Locale(
    val code: String,
    val language: String,
    val country: String,
    val name: String,
    val nativeName: String,
    val defaultCurrency: String,
    val numberFormat: String,
    val dateFormat: String
)

/**
 * Currency data class
 */
data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    val decimalPlaces: Int = 2
)
