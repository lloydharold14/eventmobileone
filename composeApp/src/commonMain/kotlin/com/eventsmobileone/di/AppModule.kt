package com.eventsmobileone.di

import com.eventsmobileone.events.di.eventsModule
import com.eventsmobileone.filter.di.filterModule
import com.eventsmobileone.locationsearch.di.locationSearchModule
import com.eventsmobileone.auth.di.authModule
import com.eventsmobileone.repository.MockLocationService
import org.koin.dsl.module

/**
 * Main application Koin module
 * Includes all feature modules and core dependencies
 */
val appModule = module {
    // Mock services for testing
    single<com.eventsmobileone.LocationService> { MockLocationService() }
    
    // Authentication dependencies
    single<com.eventsmobileone.repository.AuthApiClient> { 
        com.eventsmobileone.repository.AuthApiClientImpl() 
    }
    
    single<com.eventsmobileone.repository.SecureStorage> { 
        com.eventsmobileone.repository.MockSecureStorage() 
    }
    
    single<com.eventsmobileone.repository.AuthRepository> { 
        com.eventsmobileone.repository.AuthRepositoryImpl(
            authApiClient = get(),
            secureStorage = get()
        )
    }
    
    includes(eventsModule, filterModule, locationSearchModule, authModule)
}
