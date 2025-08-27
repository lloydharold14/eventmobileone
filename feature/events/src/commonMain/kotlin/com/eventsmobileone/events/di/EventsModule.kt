package com.eventsmobileone.events.di

import com.eventsmobileone.events.EventsViewModel
import com.eventsmobileone.repository.EventsRepository
import com.eventsmobileone.repository.EventsRepositoryImpl
import com.eventsmobileone.usecase.GetCategoriesUseCase
import com.eventsmobileone.usecase.GetEventsUseCase
import com.eventsmobileone.usecase.GetEventByIdUseCase
import org.koin.dsl.module

/**
 * Koin module for Events feature
 * Provides all dependencies for the events browsing functionality
 */
val eventsModule = module {
    // Repository - Mock implementation for now
    single<EventsRepository> { EventsRepositoryImpl(eventsApiClient = get()) }
    
    // Use Cases
    factory { GetCategoriesUseCase(get()) }
    factory { GetEventsUseCase(get()) }
    factory { GetEventByIdUseCase(get()) }
    
    // ViewModel
    factory { EventsViewModel(get(), get()) }
}
