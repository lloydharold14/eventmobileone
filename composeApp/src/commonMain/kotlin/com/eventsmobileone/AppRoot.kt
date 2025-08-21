package com.eventsmobileone

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eventsmobileone.events.EventsScreen
import com.eventsmobileone.events.EventsViewModel
import com.eventsmobileone.AppTheme
import com.eventsmobileone.repository.MockEventsRepository
import com.eventsmobileone.usecase.GetCategoriesUseCase
import com.eventsmobileone.usecase.GetEventsUseCase

/**
 * Main App Root Component
 * Showcasing the EventMO Events Browsing Feature with Enhanced Filtering (eventMO-1002)
 */
@Composable
fun AppRoot() {
    AppTheme {
        val viewModel = EventsViewModel(
            getCategoriesUseCase = GetCategoriesUseCase(
                eventsRepository = MockEventsRepository()
            ),
            getEventsUseCase = GetEventsUseCase(
                eventsRepository = MockEventsRepository()
            )
        )
        EventsScreen(viewModel = viewModel)
    }
}
