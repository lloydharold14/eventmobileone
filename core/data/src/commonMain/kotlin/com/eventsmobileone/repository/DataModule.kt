package com.eventsmobileone.repository

import org.koin.dsl.module
import com.eventsmobileone.LocationService

val dataModule = module {
    // API Clients
    single<AuthApiClient> { AuthApiClientImpl() }
    single<EventsApiClient> { EventsApiClientImpl() }
    
    // Repositories
    single<AuthRepository> { 
        AuthRepositoryImpl(
            authApiClient = get(),
            secureStorage = get()
        )
    }
    
    // Secure Storage (using mock for now, will be replaced with real implementation)
    single<SecureStorage> { MockSecureStorage() }
    
    // Location Service (using mock for now, will be replaced with real implementation)
    single<LocationService> { MockLocationService() }
    
    // Events Repository
    single<EventsRepository> { 
        EventsRepositoryImpl(
            eventsApiClient = get()
        )
    }
}
