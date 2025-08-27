package com.eventsmobileone.repository

import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Extension functions for adding mobile User-Agent headers
 */
object HttpClientExtensions {
    
    /**
     * Add mobile User-Agent header to HTTP requests
     */
    fun HttpRequestBuilder.addMobileUserAgent() {
        header("User-Agent", "EventMO-Mobile/1.0.0 (Mobile)")
    }
    
    /**
     * Add mobile User-Agent header with platform-specific info
     */
    fun HttpRequestBuilder.addMobileUserAgent(platform: String) {
        header("User-Agent", "EventMO-Mobile/1.0.0 ($platform; Mobile)")
    }
}
