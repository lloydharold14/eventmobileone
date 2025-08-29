package com.eventsmobileone

/**
 * Language service interface for managing app language
 */
interface LanguageService {
    /**
     * Get the current language code
     */
    fun getCurrentLanguage(): String
    
    /**
     * Set the current language code
     */
    suspend fun setLanguage(languageCode: String)
    
    /**
     * Get supported languages
     */
    fun getSupportedLanguages(): List<Language>
}

/**
 * Language data class
 */
data class Language(
    val code: String,
    val name: String,
    val nativeName: String
)
