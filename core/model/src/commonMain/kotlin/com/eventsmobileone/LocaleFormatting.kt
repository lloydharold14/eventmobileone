package com.eventsmobileone

/**
 * Locale-specific formatting utilities
 * Provides number, currency, and date formatting based on locale
 */
object LocaleFormatting {
    
    /**
     * Format currency amount based on locale
     * @param amount Amount in smallest currency unit (e.g., cents)
     * @param currencyCode Currency code (e.g., "USD", "CAD")
     * @param localeCode Locale code (e.g., "en-US", "fr-CA")
     * @return Formatted currency string
     */
    fun formatCurrency(amount: Double, currencyCode: String, localeCode: String): String {
        val currency = getCurrencyInfo(currencyCode)
        val locale = getLocaleInfo(localeCode)
        
        return when (locale.code) {
            "fr-CA", "fr-FR", "fr-BJ", "fr-TG" -> {
                // French formatting: 1 234,56 $
                val formattedNumber = formatNumber(amount, localeCode)
                "$formattedNumber ${currency.symbol}"
            }
            "de-DE" -> {
                // German formatting: 1.234,56 €
                val formattedNumber = formatNumber(amount, localeCode)
                "$formattedNumber ${currency.symbol}"
            }
            "es-ES", "es-US", "es-MX" -> {
                // Spanish formatting: 1.234,56 €
                val formattedNumber = formatNumber(amount, localeCode)
                "$formattedNumber ${currency.symbol}"
            }
            "it-IT" -> {
                // Italian formatting: 1.234,56 €
                val formattedNumber = formatNumber(amount, localeCode)
                "$formattedNumber ${currency.symbol}"
            }
            "ja-JP" -> {
                // Japanese formatting: ¥1,234
                "${currency.symbol}${formatNumber(amount, localeCode)}"
            }
            "pt-BR" -> {
                // Brazilian Portuguese formatting: R$ 1.234,56
                "${currency.symbol} ${formatNumber(amount, localeCode)}"
            }
            "hi-IN", "en-IN" -> {
                // Indian formatting: ₹1,234.56
                "${currency.symbol}${formatNumber(amount, localeCode)}"
            }
            else -> {
                // Default English formatting: $1,234.56
                "${currency.symbol}${formatNumber(amount, localeCode)}"
            }
        }
    }
    
    /**
     * Format number based on locale
     * @param number Number to format
     * @param localeCode Locale code
     * @return Formatted number string
     */
    fun formatNumber(number: Double, localeCode: String): String {
        // Simple number formatting for multiplatform compatibility
        val formatted = when {
            number == number.toLong().toDouble() -> number.toLong().toString()
            else -> {
                val decimal = (number * 100).toInt()
                val whole = decimal / 100
                val fraction = decimal % 100
                if (fraction == 0) whole.toString() else "$whole.${fraction.toString().padStart(2, '0')}"
            }
        }
        
        return when (localeCode) {
            "fr-CA", "fr-FR", "fr-BJ", "fr-TG" -> {
                // French: 1 234,56
                formatted.replace(".", ",")
            }
            "de-DE", "es-ES", "es-US", "es-MX", "it-IT", "pt-BR" -> {
                // German/Spanish/Italian/Portuguese: 1.234,56
                formatted.replace(".", ",")
            }
            "ja-JP" -> {
                // Japanese: 1,234
                formatted.split(".")[0]
            }
            "hi-IN", "en-IN" -> {
                // Indian: 1,234.56
                formatted
            }
            else -> {
                // Default English: 1,234.56
                formatted
            }
        }
    }
    
    /**
     * Format date based on locale
     * @param dateString ISO date string
     * @param localeCode Locale code
     * @return Formatted date string
     */
    fun formatDate(dateString: String, localeCode: String): String {
        // For now, return the date as-is
        // TODO: Implement proper date formatting with kotlinx-datetime
        return dateString
    }
    
    /**
     * Get currency information
     */
    private fun getCurrencyInfo(currencyCode: String): Currency {
        val currencies = mapOf(
            "USD" to Currency("USD", "US Dollar", "$", 2),
            "CAD" to Currency("CAD", "Canadian Dollar", "$", 2),
            "EUR" to Currency("EUR", "Euro", "€", 2),
            "GBP" to Currency("GBP", "British Pound", "£", 2),
            "JPY" to Currency("JPY", "Japanese Yen", "¥", 0),
            "XOF" to Currency("XOF", "West African CFA Franc", "CFA", 0),
            "GHS" to Currency("GHS", "Ghanaian Cedi", "₵", 2),
            "NGN" to Currency("NGN", "Nigerian Naira", "₦", 2),
            "AUD" to Currency("AUD", "Australian Dollar", "$", 2),
            "MXN" to Currency("MXN", "Mexican Peso", "$", 2),
            "BRL" to Currency("BRL", "Brazilian Real", "R$", 2),
            "INR" to Currency("INR", "Indian Rupee", "₹", 2),
            "SGD" to Currency("SGD", "Singapore Dollar", "$", 2)
        )
        return currencies[currencyCode] ?: Currency("USD", "US Dollar", "$", 2)
    }
    
    /**
     * Get locale information
     */
    private fun getLocaleInfo(localeCode: String): Locale {
        val locales = mapOf(
            "en-US" to Locale("en-US", "en", "US", "English (United States)", "English (United States)", "USD", "1,234.56", "MM/DD/YYYY"),
            "en-CA" to Locale("en-CA", "en", "CA", "English (Canada)", "English (Canada)", "CAD", "1,234.56", "MM/DD/YYYY"),
            "fr-CA" to Locale("fr-CA", "fr", "CA", "French (Canada)", "Français (Canada)", "CAD", "1 234,56", "DD/MM/YYYY"),
            "en-GB" to Locale("en-GB", "en", "GB", "English (United Kingdom)", "English (United Kingdom)", "GBP", "1,234.56", "DD/MM/YYYY"),
            "fr-FR" to Locale("fr-FR", "fr", "FR", "French (France)", "Français (France)", "EUR", "1 234,56", "DD/MM/YYYY"),
            "es-US" to Locale("es-US", "es", "US", "Spanish (United States)", "Español (Estados Unidos)", "USD", "1,234.56", "MM/DD/YYYY"),
            "ja-JP" to Locale("ja-JP", "ja", "JP", "Japanese (Japan)", "日本語 (日本)", "JPY", "1,234", "YYYY/MM/DD"),
            "fr-BJ" to Locale("fr-BJ", "fr", "BJ", "French (Benin)", "Français (Bénin)", "XOF", "1 234,56", "DD/MM/YYYY"),
            "fr-TG" to Locale("fr-TG", "fr", "TG", "French (Togo)", "Français (Togo)", "XOF", "1 234,56", "DD/MM/YYYY"),
            "en-GH" to Locale("en-GH", "en", "GH", "English (Ghana)", "English (Ghana)", "GHS", "1,234.56", "DD/MM/YYYY"),
            "en-NG" to Locale("en-NG", "en", "NG", "English (Nigeria)", "English (Nigeria)", "NGN", "1,234.56", "DD/MM/YYYY"),
            "de-DE" to Locale("de-DE", "de", "DE", "German (Germany)", "Deutsch (Deutschland)", "EUR", "1.234,56", "DD.MM.YYYY"),
            "es-ES" to Locale("es-ES", "es", "ES", "Spanish (Spain)", "Español (España)", "EUR", "1.234,56", "DD/MM/YYYY"),
            "it-IT" to Locale("it-IT", "it", "IT", "Italian (Italy)", "Italiano (Italia)", "EUR", "1.234,56", "DD/MM/YYYY"),
            "en-AU" to Locale("en-AU", "en", "AU", "English (Australia)", "English (Australia)", "AUD", "1,234.56", "DD/MM/YYYY"),
            "es-MX" to Locale("es-MX", "es", "MX", "Spanish (Mexico)", "Español (México)", "MXN", "1,234.56", "DD/MM/YYYY"),
            "pt-BR" to Locale("pt-BR", "pt", "BR", "Portuguese (Brazil)", "Português (Brasil)", "BRL", "1.234,56", "DD/MM/YYYY"),
            "hi-IN" to Locale("hi-IN", "hi", "IN", "Hindi (India)", "हिन्दी (भारत)", "INR", "1,234.56", "DD/MM/YYYY"),
            "en-IN" to Locale("en-IN", "en", "IN", "English (India)", "English (India)", "INR", "1,234.56", "DD/MM/YYYY"),
            "en-SG" to Locale("en-SG", "en", "SG", "English (Singapore)", "English (Singapore)", "SGD", "1,234.56", "DD/MM/YYYY")
        )
        return locales[localeCode] ?: locales["en-US"]!!
    }
}
