# Multi-Module Architecture Visual Guide

## 🎨 Visual Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                    APP LAYER                                       │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  composeApp/                                                                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                                │
│  │ androidMain │  │  iosMain    │  │ commonMain  │                                │
│  │             │  │             │  │             │                                │
│  │ MainActivity│  │MainViewController│   App.kt  │                                │
│  │ Platform.kt │  │ Platform.kt │ MainView.kt  │                                │
│  └─────────────┘  └─────────────┘  └─────────────┘                                │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                  FEATURE LAYER                                     │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  feature/                                                                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│  │   events    │  │    auth     │  │  bookings   │  │   profile   │              │
│  │             │  │             │  │             │  │             │              │
│  │ EventsScreen│  │ LoginScreen │  │BookingScreen│  │ProfileScreen│              │
│  │ EventDetail │  │ Register    │  │ BookingHist │  │ EditProfile │              │
│  │ EventsVM    │  │ AuthVM      │  │ BookingVM   │  │ ProfileVM   │              │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘              │
│                                                                                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│  │notifications│  │   search    │  │    maps     │  │  organizer  │              │
│  │             │  │             │  │             │  │             │              │
│  │Notifications│  │ SearchScreen│  │  MapScreen  │  │OrganizerDash│              │
│  │ NotificationsVM│ SearchVM   │  │  MapVM      │  │ OrganizerVM │              │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘              │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                PRESENTATION LAYER                                  │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  ui/                                                                                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│  │    core     │  │   theme     │  │ components  │  │ navigation  │              │
│  │             │  │             │  │             │  │             │              │
│  │LoadingSpinner│  │ AppTheme   │  │ EventCard   │  │ AppNavigator│              │
│  │ ErrorView   │  │ ColorScheme│  │ TicketSelect│  │ NavGraph    │              │
│  │ EmptyState  │  │ Typography  │  │ PaymentForm │  │ BottomNav   │              │
│  │ RetryButton │  │ Dimensions  │  │ SearchBar   │  │ TopAppBar   │              │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘              │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                  DOMAIN LAYER                                      │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  core/                                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│  │   domain    │  │    data     │  │     di      │  │   common    │              │
│  │             │  │             │  │             │  │             │              │
│  │ User, Event │  │RepositoryImpl│  │ CoreModule │  │ DateUtils   │              │
│  │ Booking     │  │ ApiService  │  │ NetworkMod │  │ StringUtils │              │
│  │ Repository  │  │ Database    │  │ DatabaseMod│  │ Validation  │              │
│  │ UseCases    │  │ Mappers     │  │ RepoModule │  │ Extensions  │              │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘              │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                PLATFORM LAYER                                      │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  platform/                                                                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                                │
│  │   android   │  │     ios     │  │   common    │                                │
│  │             │  │             │  │             │                                │
│  │AndroidNetwork│  │IOSNetwork  │  │ NetworkClient│                               │
│  │AndroidDatabase│ │IOSDatabase │  │ Database    │                               │
│  │AndroidStorage │ │IOSStorage  │  │ Storage     │                               │
│  │AndroidLocation│ │IOSLocation │  │ Location    │                               │
│  │AndroidNotif  │ │IOSNotif     │  │ Notification│                               │
│  │AndroidBiometric││IOSBiometric│  │ Biometric   │                               │
│  └─────────────┘  └─────────────┘  └─────────────┘                                │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

## 🔄 Dependency Flow Visualization

```
                    ┌─────────────┐
                    │ composeApp  │
                    │   (App)     │
                    └─────┬───────┘
                          │
                    ┌─────▼───────┐
                    │  Features   │
                    │  (Events,   │
                    │   Auth,     │
                    │  Bookings)  │
                    └─────┬───────┘
                          │
                    ┌─────▼───────┐
                    │     UI      │
                    │  (Core,     │
                    │  Theme,     │
                    │ Components) │
                    └─────┬───────┘
                          │
                    ┌─────▼───────┐
                    │    Core     │
                    │  (Domain,   │
                    │   Data,     │
                    │     DI)     │
                    └─────┬───────┘
                          │
                    ┌─────▼───────┐
                    │  Platform   │
                    │ (Android,   │
                    │    iOS,     │
                    │  Common)    │
                    └─────────────┘
```

## 🏗️ Module Boundaries and Communication

### 1. Layer Boundaries

#### App Layer → Feature Layer
- **Direction**: App layer depends on feature modules
- **Communication**: Feature interfaces and navigation coordination
- **Data Flow**: Feature modules expose composable screens

```kotlin
// App layer coordinates features
class App(
    private val eventsFeature: EventsFeature,
    private val authFeature: AuthFeature
) {
    fun createMainView(): @Composable () -> Unit {
        // Coordinate between features
    }
}
```

#### Feature Layer → UI Layer
- **Direction**: Feature modules depend on UI components
- **Communication**: Reusable UI components and design system
- **Data Flow**: UI components receive data from feature ViewModels

```kotlin
// Feature uses UI components
@Composable
fun EventsScreen(viewModel: EventsViewModel) {
    EventList(
        events = viewModel.events,
        onEventClick = viewModel::onEventClick
    )
}
```

#### Feature Layer → Core Layer
- **Direction**: Feature modules depend on core business logic
- **Communication**: Use cases and repository interfaces
- **Data Flow**: Features call use cases, use cases call repositories

```kotlin
// Feature calls use cases
class EventsViewModel(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {
    fun loadEvents() {
        viewModelScope.launch {
            val events = getEventsUseCase()
            // Update UI state
        }
    }
}
```

#### Core Layer → Platform Layer
- **Direction**: Core layer depends on platform interfaces
- **Communication**: Platform-agnostic interfaces with platform-specific implementations
- **Data Flow**: Repositories use platform services for data access

```kotlin
// Core uses platform interfaces
class EventRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: Database
) : EventRepository {
    // Implementation uses platform services
}
```

### 2. Communication Patterns

#### 2.1 Dependency Injection Pattern

```kotlin
// Feature modules expose their dependencies
interface EventsFeature {
    fun createEventsScreen(): @Composable () -> Unit
    fun createEventDetailScreen(eventId: String): @Composable () -> Unit
}

// App module coordinates feature modules
class App(
    private val eventsFeature: EventsFeature,
    private val authFeature: AuthFeature,
    private val bookingsFeature: BookingsFeature
) {
    fun createMainView(): @Composable () -> Unit {
        // Coordinate between features
    }
}
```

#### 2.2 Event-Driven Communication

```kotlin
// Shared events between modules
sealed class AppEvent {
    data class UserLoggedIn(val user: User) : AppEvent()
    data class EventBooked(val booking: Booking) : AppEvent()
    data class NotificationReceived(val notification: Notification) : AppEvent()
    data class UserLoggedOut : AppEvent()
}

// Event bus for cross-module communication
class EventBus {
    private val _events = MutableSharedFlow<AppEvent>()
    val events = _events.asSharedFlow()
    
    suspend fun emit(event: AppEvent) {
        _events.emit(event)
    }
}

// Features listen to events
class EventsViewModel(
    private val eventBus: EventBus
) : ViewModel() {
    init {
        viewModelScope.launch {
            eventBus.events.collect { event ->
                when (event) {
                    is AppEvent.UserLoggedIn -> refreshEvents()
                    is AppEvent.UserLoggedOut -> clearEvents()
                }
            }
        }
    }
}
```

#### 2.3 Navigation Coordination

```kotlin
// Centralized navigation
class AppNavigator(
    private val eventsFeature: EventsFeature,
    private val authFeature: AuthFeature,
    private val bookingsFeature: BookingsFeature
) {
    fun navigateToEvent(eventId: String) {
        // Coordinate navigation between features
    }
    
    fun navigateToBooking(bookingId: String) {
        // Handle deep linking
    }
    
    fun navigateToProfile() {
        // Navigate to profile feature
    }
}
```

#### 2.4 State Management

```kotlin
// Shared state between features
data class AppState(
    val user: User? = null,
    val isAuthenticated: Boolean = false,
    val currentEvent: Event? = null,
    val notifications: List<Notification> = emptyList()
)

// Global state management
class AppStateManager {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()
    
    fun updateUser(user: User?) {
        _state.value = _state.value.copy(
            user = user,
            isAuthenticated = user != null
        )
    }
}
```

## 📋 Implementation Guidelines

### 1. Module Creation Checklist

#### For Feature Modules:
- [ ] Create module structure with `commonMain`, `androidMain`, `iosMain`
- [ ] Define public API interface
- [ ] Implement ViewModel with state management
- [ ] Create UI screens using Compose
- [ ] Add platform-specific implementations
- [ ] Configure dependency injection
- [ ] Write unit tests for business logic
- [ ] Add UI tests for screens

#### For Core Modules:
- [ ] Define domain entities and use cases
- [ ] Implement repository interfaces
- [ ] Create data layer with API and database
- [ ] Add mappers for data transformation
- [ ] Configure dependency injection modules
- [ ] Write comprehensive unit tests
- [ ] Add integration tests

#### For UI Modules:
- [ ] Create reusable components
- [ ] Implement design system
- [ ] Add platform-specific adaptations
- [ ] Write component tests
- [ ] Document component APIs

#### For Platform Modules:
- [ ] Define platform-agnostic interfaces
- [ ] Implement Android-specific code
- [ ] Implement iOS-specific code
- [ ] Add platform tests
- [ ] Handle platform-specific permissions

### 2. Dependency Management

#### Dependency Rules:
1. **No circular dependencies** between modules
2. **Feature modules** can depend on core and UI modules
3. **Core modules** can only depend on platform modules
4. **Platform modules** have no external dependencies
5. **UI modules** can depend on core modules

#### Dependency Injection:
```kotlin
// Feature module DI
val eventsModule = module {
    // ViewModels
    viewModel { EventsViewModel(get(), get()) }
    
    // Use cases
    single { GetEventsUseCase(get()) }
    single { SearchEventsUseCase(get()) }
    
    // Feature interface
    single<EventsFeature> { EventsFeatureImpl(get()) }
}

// Core module DI
val coreModule = module {
    // Repositories
    single<EventRepository> { EventRepositoryImpl(get(), get()) }
    
    // Data sources
    single { createApiService() }
    single { createDatabase() }
    
    // Platform services
    single<NetworkClient> { get<PlatformNetworkClient>() }
    single<Database> { get<PlatformDatabase>() }
}
```

### 3. Testing Strategy

#### Unit Testing:
```kotlin
// Test use cases
class GetEventsUseCaseTest {
    private val mockRepository: EventRepository = mockk()
    private val useCase = GetEventsUseCase(mockRepository)
    
    @Test
    fun `should return events when repository succeeds`() = runTest {
        // Given
        val events = listOf(Event(id = "1", title = "Test Event"))
        coEvery { mockRepository.getEvents(any(), any()) } returns PagedResult(events, true)
        
        // When
        val result = useCase(EventFilters(), 0, 10)
        
        // Then
        assertEquals(events, result.data)
        assertTrue(result.hasMorePages)
    }
}
```

#### UI Testing:
```kotlin
// Test UI components
@RunWith(AndroidJUnit4::class)
class EventsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun eventsScreen_displaysEvents() {
        // Given
        val events = listOf(Event(id = "1", title = "Test Event"))
        
        // When
        composeTestRule.setContent {
            EventsScreen(events = events)
        }
        
        // Then
        composeTestRule.onNodeWithText("Test Event").assertIsDisplayed()
    }
}
```

### 4. Performance Considerations

#### Module Loading:
- **Lazy loading** of feature modules
- **Progressive loading** of UI components
- **Efficient dependency resolution**

#### Memory Management:
- **Proper disposal** of resources
- **Memory leak prevention** in ViewModels
- **Efficient image loading** and caching

#### Network Optimization:
- **Request batching** and caching
- **Offline-first** architecture
- **Background sync** strategies

This architecture provides a solid foundation for building a scalable, maintainable event management app with clear boundaries, efficient communication patterns, and excellent testability.

