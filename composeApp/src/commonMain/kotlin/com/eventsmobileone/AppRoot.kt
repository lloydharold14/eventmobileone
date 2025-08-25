package com.eventsmobileone

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.eventsmobileone.events.EventsScreen
import com.eventsmobileone.events.EventsViewModel
import com.eventsmobileone.filter.FilterScreen
import com.eventsmobileone.filter.FilterViewModel
import com.eventsmobileone.locationsearch.LocationSearchScreen
import com.eventsmobileone.locationsearch.LocationSearchViewModel
import com.eventsmobileone.usecase.SearchEventsByLocationUseCase
import com.eventsmobileone.AppTheme
import com.eventsmobileone.repository.*
import com.eventsmobileone.usecase.GetCategoriesUseCase
import com.eventsmobileone.repository.AuthApiClientImpl
import com.eventsmobileone.repository.EventsApiClientImpl
import com.eventsmobileone.repository.AuthRepositoryImpl
import com.eventsmobileone.repository.EventsRepositoryImpl
import com.eventsmobileone.repository.MockSecureStorage
import com.eventsmobileone.repository.MockLocationService
import com.eventsmobileone.usecase.GetEventsUseCase
import com.eventsmobileone.auth.AuthViewModel
import com.eventsmobileone.auth.LoginScreen
import com.eventsmobileone.auth.SignUpScreen
import com.eventsmobileone.events.EventDetailScreen
import com.eventsmobileone.usecase.SignInUseCase
import com.eventsmobileone.usecase.SignInWithOAuthUseCase
import com.eventsmobileone.usecase.SignUpUseCase
import com.eventsmobileone.usecase.SignOutUseCase
import com.eventsmobileone.usecase.GetCurrentUserUseCase
import com.eventsmobileone.ui.createDispatcherProvider
import kotlinx.coroutines.launch

/**
 * Main App Root Component
 * Showcasing the EventMO Events Browsing Feature with Enhanced Filtering (eventMO-1002)
 */
@Composable
fun AppRoot() {
    AppTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
        var currentFilters by remember { mutableStateOf<com.eventsmobileone.EventFilter?>(null) }
        var selectedEvent by remember { mutableStateOf<Event?>(null) }
        
        val dispatcherProvider = remember { createDispatcherProvider() }
        val scope = rememberCoroutineScope()
        
                       // Use real API repositories
               val authApiClient = remember { AuthApiClientImpl() }
               val eventsApiClient = remember { EventsApiClientImpl() }
               val secureStorage = remember { MockSecureStorage() }
               val locationService = remember { MockLocationService() }
               
               val authRepository = remember { 
                   AuthRepositoryImpl(authApiClient, secureStorage) 
               }
               val eventsRepository = remember { 
                   EventsRepositoryImpl(eventsApiClient) 
               }
            
            val eventsViewModel = remember {
                EventsViewModel(
                    getCategoriesUseCase = GetCategoriesUseCase(eventsRepository),
                    getEventsUseCase = GetEventsUseCase(eventsRepository)
                )
            }
        
        val filterViewModel = remember {
            FilterViewModel(initialFilters = currentFilters)
        }
        
        val locationSearchViewModel = remember {
            LocationSearchViewModel(
                searchEventsByLocationUseCase = SearchEventsByLocationUseCase(
                    eventsRepository = eventsRepository,
                    locationService = locationService
                ),
                locationService = locationService
            )
        }
        
        val authViewModel = remember {
            AuthViewModel(
                signInUseCase = SignInUseCase(authRepository, dispatcherProvider.io),
                signUpUseCase = SignUpUseCase(authRepository, dispatcherProvider.io),
                signInWithOAuthUseCase = SignInWithOAuthUseCase(authRepository),
                signOutUseCase = SignOutUseCase(authRepository, dispatcherProvider.io),
                getCurrentUserUseCase = GetCurrentUserUseCase(authRepository, dispatcherProvider.io),
                dispatcher = dispatcherProvider.main
            )
        }
        
        // Handle navigation from Events screen
        LaunchedEffect(eventsViewModel) {
            eventsViewModel.effect.collect { effect ->
                when (effect) {
                    is com.eventsmobileone.events.EventsEffect.NavigateToFilterScreen -> {
                        currentScreen = Screen.Filter
                    }
                    is com.eventsmobileone.events.EventsEffect.NavigateToLocationSearch -> {
                        currentScreen = Screen.LocationSearch
                    }
                    is com.eventsmobileone.events.EventsEffect.NavigateToEventDetail -> {
                        // Load event details and navigate
                        scope.launch {
                            val eventResult = eventsRepository.getEventById(effect.eventId)
                            if (eventResult.isSuccess) {
                                selectedEvent = eventResult.getOrNull()
                                currentScreen = Screen.EventDetail(effect.eventId)
                            }
                        }
                    }
                    is com.eventsmobileone.events.EventsEffect.ShowError -> {
                        // FIXME(eventMO-1014): Show error - will be implemented with error handling
                        println("Show error: ${effect.message}")
                    }
                }
            }
        }
        
        // Handle navigation from Filter screen
        LaunchedEffect(filterViewModel) {
            filterViewModel.effect.collect { effect ->
                when (effect) {
                    is com.eventsmobileone.filter.FilterEffect.ApplyFilters -> {
                        // Update current filters and go back to events
                        currentFilters = com.eventsmobileone.EventFilter(
                            dateRange = effect.dateRange,
                            priceRange = effect.priceRange,
                            availability = effect.availability,
                            sortBy = effect.sortBy
                        )
                        currentScreen = Screen.Events
                    }
                    is com.eventsmobileone.filter.FilterEffect.NavigateBack -> {
                        currentScreen = Screen.Events
                    }
                    is com.eventsmobileone.filter.FilterEffect.ShowError -> {
                        // FIXME(eventMO-1013): Show error - will be implemented with error handling
                        println("Show error: ${effect.message}")
                    }
                }
            }
        }
        
        // Handle authentication navigation
        LaunchedEffect(authViewModel) {
            authViewModel.effect.collect { effect ->
                when (effect) {
                    is AuthEffect.NavigateToHome -> {
                        currentScreen = Screen.Events
                    }
                    is AuthEffect.NavigateToOnboarding -> {
                        currentScreen = Screen.Events // For now, go directly to events
                    }
                    is AuthEffect.NavigateToSignUp -> {
                        currentScreen = Screen.SignUp
                    }
                    is AuthEffect.NavigateToForgotPassword -> {
                        // TODO: Implement forgot password screen
                        println("Navigate to forgot password")
                    }
                    is AuthEffect.ShowError -> {
                        println("Auth error: ${effect.error.userFriendlyMessage}")
                    }
                    is AuthEffect.ShowSuccess -> {
                        println("Auth success: ${effect.message}")
                    }
                }
            }
        }
        
        // Collect AuthViewModel state reactively
        val authState by authViewModel.state.collectAsState()
        
        when (currentScreen) {
            Screen.Login -> {
                LoginScreen(
                    state = authState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToSignUp = { currentScreen = Screen.SignUp },
                    onNavigateToForgotPassword = { /* TODO: Implement forgot password */ }
                )
            }
            Screen.SignUp -> {
                SignUpScreen(
                    state = authState,
                    onEvent = authViewModel::onEvent,
                    onNavigateToLogin = { currentScreen = Screen.Login }
                )
            }
            Screen.Events -> {
                EventsScreen(viewModel = eventsViewModel)
            }
            Screen.Filter -> {
                FilterScreen(viewModel = filterViewModel)
            }
            Screen.LocationSearch -> {
                LocationSearchScreen(viewModel = locationSearchViewModel)
            }
            is Screen.EventDetail -> {
                selectedEvent?.let { event ->
                    EventDetailScreen(
                        event = event,
                        onNavigateBack = { currentScreen = Screen.Events },
                        onBookEvent = { event ->
                            // TODO: Implement booking flow
                            println("Book event: ${event.title}")
                        }
                    )
                } ?: run {
                    // Show loading or error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}


