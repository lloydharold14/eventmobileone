package com.eventsmobileone

/**
 * Application configuration for different environments
 * Following Kotlin API guidelines for testability and maintainability
 * 
 * This object provides environment-specific configuration that can be used
 * across all platforms (Android, iOS, Desktop).
 */
object AppConfig {
    /**
     * API base URL for the current environment
     * This will be set at build time or runtime based on the environment
     */
    val API_BASE_URL: String = getApiBaseUrl()
    
    /**
     * Environment name
     */
    val ENVIRONMENT: String = getEnvironment()
    
    /**
     * Whether this is a debug build
     */
    val IS_DEBUG: Boolean = getIsDebug()
    
    /**
     * Get API base URL based on environment
     * Default to production, can be overridden at build time
     */
    private fun getApiBaseUrl(): String {
        return when (getEnvironment()) {
            "dev" -> "https://dev-api.event.tkhtech.com"
            "staging" -> "https://staging-api.event.tkhtech.com"
            "prod" -> "https://api.event.tkhtech.com"
            else -> "https://api.event.tkhtech.com" // fallback to prod
        }
    }
    
    /**
     * Get environment name
     * Default to production, can be overridden at build time
     */
    private fun getEnvironment(): String {
        return "prod" // Default to production
    }
    
    /**
     * Get debug flag
     * Default to false for production, can be overridden at build time
     */
    private fun getIsDebug(): Boolean {
        return false // Default to production mode
    }
}
