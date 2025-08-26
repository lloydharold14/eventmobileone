package com.eventsmobileone.repository

import org.koin.dsl.module
import com.eventsmobileone.LocationService
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
    single<AuthApiClient> { AuthApiClientImpl() }
    single<EventsApiClient> { EventsApiClientImpl() }
    single<TicketsApiClient> { 
        TicketsApiClient(
            httpClient = get(),
            baseUrl = "https://a5sma74inf.execute-api.ca-central-1.amazonaws.com/dev",
            secureStorage = get()
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
}
