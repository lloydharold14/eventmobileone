package com.eventsmobileone.locationsearch.di

import com.eventsmobileone.locationsearch.LocationSearchViewModel
import com.eventsmobileone.usecase.SearchEventsByLocationUseCase
import org.koin.dsl.module

val locationSearchModule = module {
    factory {
        SearchEventsByLocationUseCase(
            eventsRepository = get(),
            locationService = get()
        )
    }
    
    factory {
        LocationSearchViewModel(
            searchEventsByLocationUseCase = get(),
            locationService = get()
        )
    }
}
