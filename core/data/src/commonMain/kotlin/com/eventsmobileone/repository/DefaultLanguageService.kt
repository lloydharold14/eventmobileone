package com.eventsmobileone.repository

import com.eventsmobileone.LanguageService
import com.eventsmobileone.Language
import com.eventsmobileone.Locale
import com.eventsmobileone.Currency

/**
 * Default language service implementation
 * Supports 20+ locales across 15+ countries as provided by the backend
 */
class DefaultLanguageService : LanguageService {
    
    private var currentLanguage: String = "en"
    private var currentLocale: String = "en-US"
    private var currentCurrency: String = "USD"
    
    override fun getCurrentLanguage(): String = currentLanguage
    
    override fun getCurrentLocale(): String = currentLocale
    
    override fun getCurrentCurrency(): String = currentCurrency
    
    override suspend fun setLanguage(languageCode: String) {
        currentLanguage = languageCode.lowercase()
        // Update locale to match language if needed
        val supportedLocale = getSupportedLocales().find { it.language == currentLanguage }
        supportedLocale?.let { setLocale(it.code) }
    }
    
    override suspend fun setLocale(localeCode: String) {
        currentLocale = localeCode
        currentLanguage = localeCode.split("-")[0].lowercase()
        // Update currency to match locale
        val locale = getSupportedLocales().find { it.code == localeCode }
        locale?.let { setCurrency(it.defaultCurrency) }
    }
    
    override suspend fun setCurrency(currencyCode: String) {
        currentCurrency = currencyCode.uppercase()
    }
    
    override fun getSupportedLanguages(): List<Language> = listOf(
        Language("en", "English", "English"),
        Language("fr", "French", "Français"),
        Language("es", "Spanish", "Español"),
        Language("de", "German", "Deutsch"),
        Language("it", "Italian", "Italiano"),
        Language("ja", "Japanese", "日本語"),
        Language("pt", "Portuguese", "Português"),
        Language("hi", "Hindi", "हिन्दी")
    )
    
    override fun getSupportedLocales(): List<Locale> = listOf(
        // Primary Markets
        Locale("en-US", "en", "US", "English (United States)", "English (United States)", "USD", "1,234.56", "MM/DD/YYYY"),
        Locale("en-CA", "en", "CA", "English (Canada)", "English (Canada)", "CAD", "1,234.56", "MM/DD/YYYY"),
        Locale("fr-CA", "fr", "CA", "French (Canada)", "Français (Canada)", "CAD", "1 234,56", "DD/MM/YYYY"),
        Locale("en-GB", "en", "GB", "English (United Kingdom)", "English (United Kingdom)", "GBP", "1,234.56", "DD/MM/YYYY"),
        Locale("fr-FR", "fr", "FR", "French (France)", "Français (France)", "EUR", "1 234,56", "DD/MM/YYYY"),
        Locale("es-US", "es", "US", "Spanish (United States)", "Español (Estados Unidos)", "USD", "1,234.56", "MM/DD/YYYY"),
        Locale("ja-JP", "ja", "JP", "Japanese (Japan)", "日本語 (日本)", "JPY", "1,234", "YYYY/MM/DD"),
        
        // African Markets
        Locale("fr-BJ", "fr", "BJ", "French (Benin)", "Français (Bénin)", "XOF", "1 234,56", "DD/MM/YYYY"),
        Locale("fr-TG", "fr", "TG", "French (Togo)", "Français (Togo)", "XOF", "1 234,56", "DD/MM/YYYY"),
        Locale("en-GH", "en", "GH", "English (Ghana)", "English (Ghana)", "GHS", "1,234.56", "DD/MM/YYYY"),
        Locale("en-NG", "en", "NG", "English (Nigeria)", "English (Nigeria)", "NGN", "1,234.56", "DD/MM/YYYY"),
        
        // European Markets
        Locale("de-DE", "de", "DE", "German (Germany)", "Deutsch (Deutschland)", "EUR", "1.234,56", "DD.MM.YYYY"),
        Locale("es-ES", "es", "ES", "Spanish (Spain)", "Español (España)", "EUR", "1.234,56", "DD/MM/YYYY"),
        Locale("it-IT", "it", "IT", "Italian (Italy)", "Italiano (Italia)", "EUR", "1.234,56", "DD/MM/YYYY"),
        
        // Other Markets
        Locale("en-AU", "en", "AU", "English (Australia)", "English (Australia)", "AUD", "1,234.56", "DD/MM/YYYY"),
        Locale("es-MX", "es", "MX", "Spanish (Mexico)", "Español (México)", "MXN", "1,234.56", "DD/MM/YYYY"),
        Locale("pt-BR", "pt", "BR", "Portuguese (Brazil)", "Português (Brasil)", "BRL", "1.234,56", "DD/MM/YYYY"),
        Locale("hi-IN", "hi", "IN", "Hindi (India)", "हिन्दी (भारत)", "INR", "1,234.56", "DD/MM/YYYY"),
        Locale("en-IN", "en", "IN", "English (India)", "English (India)", "INR", "1,234.56", "DD/MM/YYYY"),
        Locale("en-SG", "en", "SG", "English (Singapore)", "English (Singapore)", "SGD", "1,234.56", "DD/MM/YYYY")
    )
    
    override fun getSupportedCurrencies(): List<Currency> = listOf(
        Currency("USD", "US Dollar", "$", 2),
        Currency("CAD", "Canadian Dollar", "$", 2),
        Currency("EUR", "Euro", "€", 2),
        Currency("GBP", "British Pound", "£", 2),
        Currency("JPY", "Japanese Yen", "¥", 0),
        Currency("XOF", "West African CFA Franc", "CFA", 0),
        Currency("GHS", "Ghanaian Cedi", "₵", 2),
        Currency("NGN", "Nigerian Naira", "₦", 2),
        Currency("AUD", "Australian Dollar", "$", 2),
        Currency("MXN", "Mexican Peso", "$", 2),
        Currency("BRL", "Brazilian Real", "R$", 2),
        Currency("INR", "Indian Rupee", "₹", 2),
        Currency("SGD", "Singapore Dollar", "$", 2)
    )
    
    override fun getAcceptLanguageHeader(): String {
        val locale = getSupportedLocales().find { it.code == currentLocale }
        return locale?.code ?: "en-US"
    }
    
    override suspend fun detectSystemLocale(): String {
        // For now, return default locale
        // TODO: Implement platform-specific locale detection
        return "en-US"
    }
}
