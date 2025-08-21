# Technical Architecture

## Kotlin Multiplatform (KMP) Architecture

### Module Structure

```
EventApp/
├── composeApp/                    # Main app entry point
│   ├── androidMain/              # Android-specific code
│   ├── iosMain/                  # iOS-specific code
│   └── commonMain/               # Shared UI components
├── core/                         # Shared business logic
│   ├── domain/                   # Use cases, entities, repositories
│   ├── data/                     # Data sources, repositories impl
│   └── di/                       # Dependency injection
├── feature/                      # Feature modules
│   ├── auth/                     # Authentication
│   ├── events/                   # Event discovery and management
│   ├── booking/                  # Ticket booking flow
│   ├── profile/                  # User profile management
│   └── notifications/            # Push notifications
├── ui/                          # Shared UI components
│   ├── core/                    # Core UI components
│   ├── theme/                   # Design system
│   └── components/              # Reusable components
└── data/                        # Data layer
    ├── local/                   # Local database
    ├── remote/                  # API clients
    └── repository/              # Repository implementations
```

## Core Module Architecture

### Domain Layer (`core/domain`)

#### Entities
```kotlin
// Core business entities
data class User(
    val id: String,
    val email: String,
    val name: String,
    val userType: UserType,
    val preferences: UserPreferences
)

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val category: EventCategory,
    val venue: Venue,
    val startDateTime: Instant,
    val endDateTime: Instant,
    val status: EventStatus
)

data class Booking(
    val id: String,
    val userId: String,
    val eventId: String,
    val ticketId: String,
    val quantity: Int,
    val status: BookingStatus
)
```

#### Use Cases
```kotlin
// Event discovery use cases
class GetEventsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        filters: EventFilters,
        page: Int,
        pageSize: Int
    ): PagedResult<Event>
}

class SearchEventsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(
        query: String,
        location: Location?,
        radius: Double
    ): List<Event>
}

// Booking use cases
class BookEventUseCase(
    private val bookingRepository: BookingRepository,
    private val paymentService: PaymentService
) {
    suspend operator fun invoke(
        bookingRequest: BookingRequest
    ): Result<Booking>
}
```

#### Repository Interfaces
```kotlin
interface EventRepository {
    suspend fun getEvents(filters: EventFilters, page: Int): PagedResult<Event>
    suspend fun getEventById(id: String): Event?
    suspend fun searchEvents(query: String, location: Location?): List<Event>
    suspend fun getEventCategories(): List<EventCategory>
}

interface BookingRepository {
    suspend fun createBooking(booking: Booking): Result<Booking>
    suspend fun getBookings(userId: String): List<Booking>
    suspend fun cancelBooking(bookingId: String): Result<Unit>
}
```

### Data Layer (`core/data`)

#### Repository Implementations
```kotlin
class EventRepositoryImpl(
    private val eventApi: EventApi,
    private val eventDao: EventDao,
    private val networkBoundResource: NetworkBoundResource
) : EventRepository {
    
    override suspend fun getEvents(
        filters: EventFilters, 
        page: Int
    ): PagedResult<Event> {
        return networkBoundResource.execute(
            query = { eventDao.getEvents(filters, page) },
            fetch = { eventApi.getEvents(filters, page) },
            saveFetchResult = { events -> eventDao.insertEvents(events) },
            shouldFetch = { it.isEmpty() || isStale() }
        )
    }
}
```

#### API Clients
```kotlin
interface EventApi {
    @GET("events")
    suspend fun getEvents(
        @Query("filters") filters: EventFilters,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<PagedResult<Event>>
    
    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: String): ApiResponse<Event>
}
```

#### Local Database
```kotlin
@Database(
    entities = [EventEntity::class, BookingEntity::class, UserEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun bookingDao(): BookingDao
    abstract fun userDao(): UserDao
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE category = :category ORDER BY startDateTime ASC")
    suspend fun getEventsByCategory(category: String): List<EventEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
}
```

## Feature Modules

### Authentication Module (`feature/auth`)

#### ViewModel
```kotlin
class AuthViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authUseCase.signIn(email, password)
            _uiState.value = when (result) {
                is Result.Success -> AuthUiState.Success(result.data)
                is Result.Error -> AuthUiState.Error(result.message)
            }
        }
    }
}

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
```

#### UI Components
```kotlin
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is AuthUiState.Success -> {
            LaunchedEffect(Unit) {
                onAuthSuccess()
            }
        }
        is AuthUiState.Error -> {
            // Show error message
        }
        // Other states...
    }
}
```

### Events Module (`feature/events`)

#### State Management
```kotlin
data class EventsState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filters: EventFilters = EventFilters(),
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true
)

sealed class EventsEvent {
    data class LoadEvents(val refresh: Boolean = false) : EventsEvent()
    data class ApplyFilters(val filters: EventFilters) : EventsEvent()
    data class SearchEvents(val query: String) : EventsEvent()
    object LoadMoreEvents : EventsEvent()
}
```

#### ViewModel
```kotlin
class EventsViewModel(
    private val getEventsUseCase: GetEventsUseCase,
    private val searchEventsUseCase: SearchEventsUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(EventsState())
    val state: StateFlow<EventsState> = _state.asStateFlow()
    
    fun handleEvent(event: EventsEvent) {
        when (event) {
            is EventsEvent.LoadEvents -> loadEvents(event.refresh)
            is EventsEvent.ApplyFilters -> applyFilters(event.filters)
            is EventsEvent.SearchEvents -> searchEvents(event.query)
            is EventsEvent.LoadMoreEvents -> loadMoreEvents()
        }
    }
    
    private fun loadEvents(refresh: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            val currentPage = if (refresh) 0 else _state.value.currentPage
            val result = getEventsUseCase(
                filters = _state.value.filters,
                page = currentPage,
                pageSize = PAGE_SIZE
            )
            
            _state.value = _state.value.copy(
                events = if (refresh) result.data else _state.value.events + result.data,
                isLoading = false,
                currentPage = currentPage + 1,
                hasMorePages = result.hasMorePages
            )
        }
    }
}
```

## Dependency Injection

### Koin Configuration
```kotlin
val coreModule = module {
    // Database
    single { createDatabase() }
    single { get<AppDatabase>().eventDao() }
    single { get<AppDatabase>().bookingDao() }
    
    // API
    single { createApiClient() }
    single { get<ApiClient>().eventApi }
    single { get<ApiClient>().bookingApi }
    
    // Repositories
    single<EventRepository> { EventRepositoryImpl(get(), get(), get()) }
    single<BookingRepository> { BookingRepositoryImpl(get(), get(), get()) }
    
    // Use Cases
    single { GetEventsUseCase(get()) }
    single { SearchEventsUseCase(get()) }
    single { BookEventUseCase(get(), get()) }
}

val featureModule = module {
    // ViewModels
    viewModel { EventsViewModel(get(), get()) }
    viewModel { BookingViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
}
```

## Platform-Specific Implementations

### Android Platform (`androidMain`)

#### Platform Services
```kotlin
actual class PlatformService {
    actual suspend fun getCurrentLocation(): Location {
        // Android-specific location implementation
        return LocationManager.getCurrentLocation()
    }
    
    actual fun showNotification(title: String, message: String) {
        // Android notification implementation
        NotificationManager.showNotification(title, message)
    }
}
```

#### Permissions
```kotlin
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onPermissionGranted()
    }
    
    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
```

### iOS Platform (`iosMain`)

#### Platform Services
```kotlin
actual class PlatformService {
    actual suspend fun getCurrentLocation(): Location {
        // iOS-specific location implementation
        return CLLocationManager.getCurrentLocation()
    }
    
    actual fun showNotification(title: String, message: String) {
        // iOS notification implementation
        UNUserNotificationCenter.showNotification(title, message)
    }
}
```

## Real-Time Features

### WebSocket Implementation
```kotlin
class WebSocketManager(
    private val url: String,
    private val scope: CoroutineScope
) {
    private var webSocket: WebSocket? = null
    
    fun connect() {
        scope.launch {
            webSocket = WebSocket.Builder()
                .url(url)
                .build()
                .connect()
        }
    }
    
    fun sendMessage(message: String) {
        webSocket?.send(message)
    }
    
    fun observeMessages(): Flow<String> = flow {
        webSocket?.messages?.collect { message ->
            emit(message)
        }
    }
}
```

### Push Notifications
```kotlin
class NotificationManager(
    private val firebaseMessaging: FirebaseMessaging,
    private val notificationService: NotificationService
) {
    fun subscribeToTopic(topic: String) {
        firebaseMessaging.subscribeToTopic(topic)
    }
    
    fun sendNotification(notification: Notification) {
        notificationService.sendNotification(notification)
    }
}
```

## Offline Capabilities

### Sync Manager
```kotlin
class SyncManager(
    private val database: AppDatabase,
    private val apiClient: ApiClient,
    private val scope: CoroutineScope
) {
    private val syncQueue = MutableSharedFlow<SyncAction>()
    
    init {
        scope.launch {
            syncQueue.collect { action ->
                processSyncAction(action)
            }
        }
    }
    
    fun queueAction(action: SyncAction) {
        scope.launch {
            syncQueue.emit(action)
        }
    }
    
    private suspend fun processSyncAction(action: SyncAction) {
        when (action) {
            is SyncAction.CreateBooking -> {
                try {
                    val booking = apiClient.createBooking(action.booking)
                    database.bookingDao().insertBooking(booking.toEntity())
                } catch (e: Exception) {
                    // Handle sync failure
                }
            }
        }
    }
}
```

## Testing Strategy

### Unit Tests
```kotlin
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

### UI Tests
```kotlin
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

## Performance Considerations

### Image Loading
```kotlin
@Composable
fun AsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
```

### Lazy Loading
```kotlin
@Composable
fun EventsList(
    events: List<Event>,
    onEventClick: (Event) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyColumn {
        items(events) { event ->
            EventCard(
                event = event,
                onClick = { onEventClick(event) }
            )
        }
        
        item {
            if (events.isNotEmpty()) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }
        }
    }
}
```

This architecture provides a solid foundation for building a scalable, maintainable event management app with Kotlin Multiplatform, following clean architecture principles and modern mobile development best practices.

