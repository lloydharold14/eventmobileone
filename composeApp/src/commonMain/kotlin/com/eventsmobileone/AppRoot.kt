package com.eventsmobileone

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.eventsmobileone.events.EventsScreen
import com.eventsmobileone.events.EventsViewModel
import com.eventsmobileone.EventTheme
import org.koin.compose.koinViewModel

/**
 * Main App Root Component
 * Showcasing the EventMO Events Browsing Feature with Enhanced Filtering (eventMO-1002)
 */
@Composable
fun AppRoot() {
    EventTheme {
        val viewModel: EventsViewModel = koinViewModel()
        EventsScreen(viewModel = viewModel)
    }
}
