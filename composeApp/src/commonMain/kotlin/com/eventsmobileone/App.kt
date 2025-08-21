package com.eventsmobileone

import androidx.compose.runtime.Composable
import com.eventsmobileone.di.appModule
import org.koin.compose.KoinApplication

/**
 * Main App Entry Point
 * This is the main composable that gets called from platform-specific launchers
 */
@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AppRoot()
    }
}