package com.eventsmobileone.di

import com.eventsmobileone.events.di.eventsModule
import org.koin.dsl.module

/**
 * Main application Koin module
 * Includes all feature modules and core dependencies
 */
val appModule = module {
    includes(eventsModule)
}
