package com.eventsmobileone.di

import com.eventsmobileone.events.di.eventsModule
import com.eventsmobileone.filter.di.filterModule
import com.eventsmobileone.locationsearch.di.locationSearchModule
import com.eventsmobileone.repository.MockLocationService
import org.koin.dsl.module

/**
 * Main application Koin module
 * Includes all feature modules and core dependencies
 */
val appModule = module {
    // Mock services for testing
    single<com.eventsmobileone.LocationService> { MockLocationService() }
    
    includes(eventsModule, filterModule, locationSearchModule)
}
