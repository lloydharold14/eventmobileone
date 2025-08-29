package com.eventsmobileone.repository

import org.koin.dsl.module
import com.eventsmobileone.LocationService
import com.eventsmobileone.LanguageService
import com.eventsmobileone.repository.DefaultLanguageService
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val dataModule = module {
    // HTTP Client
    single<HttpClient> { 
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
    
    // API Clients
    single<AuthApiClient> { AuthApiClientImpl(languageService = get()) }
    single<EventsApiClient> { EventsApiClientImpl() }
    single<TicketsApiClient> { 
        TicketsApiClient(
            httpClient = get(),
            baseUrl = "https://api.event.tkhtech.com",
            secureStorage = get()
        )
    }
    single<BookingsApiClient> { 
        BookingsApiClientImpl(
            httpClient = get(),
            baseUrl = "https://api.event.tkhtech.com"
        )
    }
    single<NotificationsApiClient> { 
        NotificationsApiClientImpl(
            httpClient = get(),
            baseUrl = "https://api.event.tkhtech.com"
        )
    }
    single<SearchApiClient> { 
        SearchApiClientImpl(
            httpClient = get(),
            baseUrl = "https://api.event.tkhtech.com"
        )
    }
    
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
    
    // Language Service
    single<LanguageService> { DefaultLanguageService() }
    
    // Events Repository
    single<EventsRepository> { 
        EventsRepositoryImpl(
            eventsApiClient = get()
        )
    }
    
    // Tickets Repository - Using real API
    single<TicketsRepository> { 
        TicketsRepositoryImpl(
            ticketsApiClient = get()
        )
    }
    
    // Bookings Repository - Using real API
    single<BookingsRepository> { 
        BookingsRepositoryImpl(
            bookingsApiClient = get()
        )
    }
    
    // Notifications Repository - Using real API
    single<NotificationsRepository> { 
        NotificationsRepositoryImpl(
            notificationsApiClient = get()
        )
    }
    
    // Search Repository - Using real API
    single<SearchRepository> { 
        SearchRepositoryImpl(
            searchApiClient = get()
        )
    }
}
