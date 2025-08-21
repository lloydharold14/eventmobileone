package com.eventsmobileone

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eventsmobileone.events.EventsScreen
import com.eventsmobileone.events.EventsViewModel
import com.eventsmobileone.EventTheme

/**
 * Main App Root Component
 * Showcasing the EventMO Events Browsing Feature (eventMO-1001)
 * Complete implementation with categories, search, and event listing
 */
@Composable
fun AppRoot() {
    EventTheme {
        // Manual injection for now - will be replaced with proper Koin injection
        val viewModel = EventsViewModel(
            getCategoriesUseCase = com.eventsmobileone.usecase.GetCategoriesUseCase(
                eventsRepository = com.eventsmobileone.repository.MockEventsRepository()
            ),
            getEventsUseCase = com.eventsmobileone.usecase.GetEventsUseCase(
                eventsRepository = com.eventsmobileone.repository.MockEventsRepository()
            )
        )
        EventsScreen(viewModel = viewModel)
    }
}
