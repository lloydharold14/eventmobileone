package com.eventsmobileone.repository

import com.eventsmobileone.LanguageService
import com.eventsmobileone.Language

/**
 * Default language service implementation
 */
class DefaultLanguageService : LanguageService {
    
    private var currentLanguage: String = "en"
    
    override fun getCurrentLanguage(): String = currentLanguage
    
    override suspend fun setLanguage(languageCode: String) {
        currentLanguage = languageCode.lowercase()
    }
    
    override fun getSupportedLanguages(): List<Language> = listOf(
        Language("en", "English", "English"),
        Language("fr", "French", "Français")
    )
}
