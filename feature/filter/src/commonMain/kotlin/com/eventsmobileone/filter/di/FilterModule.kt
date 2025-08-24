package com.eventsmobileone.filter.di

import com.eventsmobileone.filter.FilterViewModel
import org.koin.dsl.module

val filterModule = module {
    factory { (initialFilters: com.eventsmobileone.EventFilter?) ->
        FilterViewModel(initialFilters)
    }
}
