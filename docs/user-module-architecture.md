# User-Centric Multi-Module Architecture
## Event Discovery & Booking App for Attendees

## Overview
This document outlines a comprehensive multi-module architecture for an event discovery and booking app built with Compose Multiplatform, specifically designed for **event attendees (users)**. The architecture follows Clean Architecture principles with a strong focus on user experience, offline-first capabilities, and seamless cross-feature interactions.

## 🏗️ Architecture Principles

### User-First Design
- **User-Centric Modules**: Every module serves the user (attendee) experience
- **Offline-First**: Users can access core features without internet
- **Social Integration**: Seamless social features across all modules
- **Personalization**: User preferences drive all interactions

### Clean Architecture Layers
- **Presentation Layer**: User-facing UI and interactions
- **Domain Layer**: User-centric business logic
- **Data Layer**: User data management and persistence
- **Platform Layer**: Platform-specific user experiences

## 📦 Module Structure

```
EventApp/
├── shared/                          # Shared user modules
│   ├── core/                       # Common utilities & user session
│   ├── domain/                     # User business logic
│   ├── data/                       # User data repositories
│   ├── network/                    # Consumer APIs
│   ├── database/                   # User data caching
│   └── ui-common/                  # User UI components
├── feature/                        # User-centric features
│   ├── discovery/                  # Event search & browsing
│   ├── auth/                       # User authentication
│   ├── profile/                    # User profile & preferences
│   ├── booking/                    # Ticket purchasing
│   ├── social/                     # Social features
│   ├── tickets/                    # Digital ticket wallet
│   └── notifications/              # User notifications
├── platform/                       # Platform-specific UX
│   ├── android/                    # Android user experience
│   └── ios/                        # iOS user experience
└── app/                           # Main app entry point
```

## 🔧 Shared Modules (User-Centric)

### shared/core
**Purpose**: Common utilities and user session management

**Responsibilities**:
- User session management and authentication state
- App configuration and user preferences
- Common utilities and extensions
- Error handling and user feedback
- Analytics and user behavior tracking

**Key Components**:
```kotlin
// User session management
interface UserSessionManager {
    suspend fun getCurrentUser(): User?
    suspend fun updateUserSession(user: User)
    suspend fun clearUserSession()
    fun observeUserSession(): Flow<User?>
}

// App configuration
interface AppConfig {
    val apiBaseUrl: String
    val appVersion: String
    val isDebug: Boolean
    val userPreferences: UserPreferences
}

// User analytics
interface UserAnalytics {
    fun trackEvent(event: UserEvent)
    fun trackUserAction(action: UserAction)
    fun trackUserJourney(journey: UserJourney)
}
```

**Dependencies**: None (base module)

### shared/domain
**Purpose**: User-centric business logic and use cases

**Responsibilities**:
- Event discovery and search logic
- Booking and payment business rules
- Social interaction logic
- User preference management
- Offline data synchronization

**Key Components**:
```kotlin
// Event discovery use cases
interface EventDiscoveryUseCase {
    suspend fun searchEvents(query: EventSearchQuery): List<Event>
    suspend fun getRecommendedEvents(userId: String): List<Event>
    suspend fun getTrendingEvents(): List<Event>
    suspend fun getEventsByCategory(category: EventCategory): List<Event>
}

// Booking use cases
interface BookingUseCase {
    suspend fun createBooking(bookingRequest: BookingRequest): Booking
    suspend fun getBookingHistory(userId: String): List<Booking>
    suspend fun cancelBooking(bookingId: String): Boolean
    suspend fun transferTicket(ticketId: String, recipientId: String): Boolean
}

// Social use cases
interface SocialUseCase {
    suspend fun getFriendsAttending(eventId: String): List<User>
    suspend fun shareEvent(eventId: String, platform: SharePlatform): Boolean
    suspend fun followUser(userId: String): Boolean
    suspend fun getSocialFeed(userId: String): List<SocialActivity>
}

// User preferences use cases
interface UserPreferencesUseCase {
    suspend fun updateUserPreferences(preferences: UserPreferences): Boolean
    suspend fun getUserPreferences(userId: String): UserPreferences
    suspend fun syncPreferencesToCloud(userId: String): Boolean
}
```

**Dependencies**: `shared/core`

### shared/data
**Purpose**: User data repositories and data management

**Responsibilities**:
- User data repositories implementation
- Data transformation and mapping
- Caching strategies for user data
- Data synchronization between local and remote

**Key Components**:
```kotlin
// User data repositories
interface UserRepository {
    suspend fun getUserProfile(userId: String): UserProfile
    suspend fun updateUserProfile(profile: UserProfile): Boolean
    suspend fun getUserPreferences(userId: String): UserPreferences
    suspend fun updateUserPreferences(preferences: UserPreferences): Boolean
}

interface EventRepository {
    suspend fun searchEvents(query: EventSearchQuery): List<Event>
    suspend fun getEventDetails(eventId: String): Event
    suspend fun getRecommendedEvents(userId: String): List<Event>
    suspend fun saveEventToWishlist(eventId: String, userId: String): Boolean
    suspend fun getWishlistEvents(userId: String): List<Event>
}

interface BookingRepository {
    suspend fun createBooking(booking: Booking): Booking
    suspend fun getBookingHistory(userId: String): List<Booking>
    suspend fun getBookingDetails(bookingId: String): Booking
    suspend fun cancelBooking(bookingId: String): Boolean
    suspend fun getActiveTickets(userId: String): List<Ticket>
}

interface SocialRepository {
    suspend fun getFriendsList(userId: String): List<User>
    suspend fun getFriendsAttending(eventId: String): List<User>
    suspend fun followUser(userId: String, targetUserId: String): Boolean
    suspend fun getSocialActivity(userId: String): List<SocialActivity>
}
```

**Dependencies**: `shared/domain`, `shared/network`, `shared/database`

### shared/network
**Purpose**: Consumer APIs for user interactions

**Responsibilities**:
- API client for user-facing services
- Event search and discovery APIs
- Booking and payment APIs
- Social and user management APIs
- Real-time updates and notifications

**Key Components**:
```kotlin
// API clients
interface EventApiClient {
    suspend fun searchEvents(query: EventSearchQuery): ApiResponse<List<Event>>
    suspend fun getEventDetails(eventId: String): ApiResponse<Event>
    suspend fun getRecommendedEvents(userId: String): ApiResponse<List<Event>>
    suspend fun getTrendingEvents(): ApiResponse<List<Event>>
}

interface BookingApiClient {
    suspend fun createBooking(request: BookingRequest): ApiResponse<Booking>
    suspend fun getBookingHistory(userId: String): ApiResponse<List<Booking>>
    suspend fun cancelBooking(bookingId: String): ApiResponse<Boolean>
    suspend fun processPayment(paymentRequest: PaymentRequest): ApiResponse<PaymentResult>
}

interface UserApiClient {
    suspend fun getUserProfile(userId: String): ApiResponse<UserProfile>
    suspend fun updateUserProfile(profile: UserProfile): ApiResponse<Boolean>
    suspend fun getUserPreferences(userId: String): ApiResponse<UserPreferences>
    suspend fun updateUserPreferences(preferences: UserPreferences): ApiResponse<Boolean>
}

interface SocialApiClient {
    suspend fun getFriendsList(userId: String): ApiResponse<List<User>>
    suspend fun getFriendsAttending(eventId: String): ApiResponse<List<User>>
    suspend fun followUser(userId: String, targetUserId: String): ApiResponse<Boolean>
    suspend fun shareEvent(shareRequest: ShareRequest): ApiResponse<Boolean>
}

// Real-time updates
interface RealtimeApiClient {
    fun subscribeToEventUpdates(eventId: String): Flow<EventUpdate>
    fun subscribeToUserActivity(userId: String): Flow<UserActivity>
    fun subscribeToBookingUpdates(userId: String): Flow<BookingUpdate>
}
```

**Dependencies**: `shared/core`

### shared/database
**Purpose**: User data caching and offline storage

**Responsibilities**:
- Local database for user data
- Offline ticket storage
- Cached event information
- User preferences and settings
- Recently viewed content

**Key Components**:
```kotlin
// Database entities
@Entity
data class UserProfileEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val profilePicture: String?,
    val location: String,
    val interests: String, // JSON
    val preferences: String, // JSON
    val lastUpdated: Long
)

@Entity
data class EventEntity(
    @PrimaryKey val eventId: String,
    val title: String,
    val description: String,
    val category: String,
    val venue: String, // JSON
    val startDateTime: Long,
    val endDateTime: Long,
    val images: String, // JSON
    val pricing: String, // JSON
    val socialMetrics: String, // JSON
    val isWishlisted: Boolean,
    val lastViewed: Long
)

@Entity
data class TicketEntity(
    @PrimaryKey val ticketId: String,
    val userId: String,
    val eventId: String,
    val ticketType: String,
    val quantity: Int,
    val totalPrice: String,
    val qrCode: String,
    val status: String,
    val purchaseDate: Long,
    val validFrom: Long,
    val validUntil: Long,
    val isDownloaded: Boolean
)

// Database access
@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE userId = :userId")
    suspend fun getUserProfile(userId: String): UserProfileEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)
    
    @Update
    suspend fun updateUserProfile(profile: UserProfileEntity)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE isWishlisted = 1 ORDER BY lastViewed DESC")
    suspend fun getWishlistedEvents(): List<EventEntity>
    
    @Query("SELECT * FROM events ORDER BY lastViewed DESC LIMIT 50")
    suspend fun getRecentlyViewedEvents(): List<EventEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)
    
    @Update
    suspend fun updateEvent(event: EventEntity)
}

@Dao
interface TicketDao {
    @Query("SELECT * FROM tickets WHERE userId = :userId AND status = 'ACTIVE'")
    suspend fun getActiveTickets(userId: String): List<TicketEntity>
    
    @Query("SELECT * FROM tickets WHERE userId = :userId ORDER BY purchaseDate DESC")
    suspend fun getTicketHistory(userId: String): List<TicketEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: TicketEntity)
    
    @Update
    suspend fun updateTicket(ticket: TicketEntity)
}
```

**Dependencies**: `shared/core`

### shared/ui-common
**Purpose**: User-facing UI components and design system

**Responsibilities**:
- Reusable UI components
- Design system implementation
- User interaction patterns
- Platform-agnostic UI elements

**Key Components**:
```kotlin
// Design system
@Composable
fun EventAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)

// Common UI components
@Composable
fun EventCard(
    event: Event,
    onEventClick: (String) -> Unit,
    onWishlistClick: (String) -> Unit,
    onShareClick: (String) -> Unit
)

@Composable
fun TicketCard(
    ticket: Ticket,
    onTicketClick: (String) -> Unit,
    onDownloadClick: (String) -> Unit
)

@Composable
fun UserProfileCard(
    user: User,
    onUserClick: (String) -> Unit,
    onFollowClick: (String) -> Unit
)

@Composable
fun BookingSummaryCard(
    booking: Booking,
    onBookingClick: (String) -> Unit,
    onCancelClick: (String) -> Unit
)

// Navigation components
@Composable
fun EventAppNavigation(
    navController: NavController,
    startDestination: String = "discovery"
)
```

**Dependencies**: `shared/core`, `shared/domain`

## 🎯 Feature Modules (User-Centric)

### feature/discovery
**Purpose**: Event search, browsing, filtering, and recommendations

**Responsibilities**:
- Event discovery and search
- Advanced filtering and sorting
- Personalized recommendations
- Event browsing experience
- Wishlist management

**Key Components**:
```kotlin
// ViewModels
class EventDiscoveryViewModel(
    private val eventDiscoveryUseCase: EventDiscoveryUseCase,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventDiscoveryUiState())
    val uiState: StateFlow<EventDiscoveryUiState> = _uiState.asStateFlow()
    
    fun searchEvents(query: EventSearchQuery)
    fun getRecommendedEvents()
    fun getTrendingEvents()
    fun saveToWishlist(eventId: String)
    fun applyFilters(filters: EventFilters)
}

// UI States
data class EventDiscoveryUiState(
    val events: List<Event> = emptyList(),
    val recommendedEvents: List<Event> = emptyList(),
    val trendingEvents: List<Event> = emptyList(),
    val filters: EventFilters = EventFilters(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Screens
@Composable
fun EventDiscoveryScreen(
    viewModel: EventDiscoveryViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit,
    onWishlistClick: (String) -> Unit
)

@Composable
fun EventSearchScreen(
    viewModel: EventDiscoveryViewModel = hiltViewModel(),
    onEventClick: (String) -> Unit
)

@Composable
fun EventFiltersScreen(
    filters: EventFilters,
    onFiltersChanged: (EventFilters) -> Unit
)
```

**Dependencies**: `shared/domain`, `shared/ui-common`, `shared/data`

### feature/auth
**Purpose**: User authentication and onboarding

**Responsibilities**:
- User registration and login
- Social authentication
- Onboarding experience
- Account verification
- Password management

**Key Components**:
```kotlin
class AuthViewModel(
    private val authUseCase: AuthUseCase,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()
    
    fun signIn(email: String, password: String)
    fun signUp(email: String, password: String, name: String)
    fun signInWithGoogle()
    fun signInWithApple()
    fun forgotPassword(email: String)
    fun signOut()
}

data class AuthUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOnboardingComplete: Boolean = false
)

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
)

@Composable
fun OnboardingScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onOnboardingComplete: () -> Unit
)
```

**Dependencies**: `shared/domain`, `shared/ui-common`, `shared/data`

### feature/profile
**Purpose**: User profile, preferences, and booking history

**Responsibilities**:
- User profile management
- Preference settings
- Booking history
- Account settings
- Privacy controls

**Key Components**:
```kotlin
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val bookingRepository: BookingRepository,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()
    
    fun loadUserProfile()
    fun updateUserProfile(profile: UserProfile)
    fun loadBookingHistory()
    fun updatePreferences(preferences: UserPreferences)
    fun exportUserData()
}

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val bookingHistory: List<Booking> = emptyList(),
    val preferences: UserPreferences? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfile: () -> Unit,
    onBookingClick: (String) -> Unit
)

@Composable
fun PreferencesScreen(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
)
```

**Dependencies**: `shared/domain`, `shared/ui-common`, `shared/data`

### feature/booking
**Purpose**: Ticket purchasing, payment, and booking management

**Responsibilities**:
- Ticket selection and purchase
- Payment processing
- Booking confirmation
- Booking management
- Refund handling

**Key Components**:
```kotlin
class BookingViewModel(
    private val bookingUseCase: BookingUseCase,
    private val paymentUseCase: PaymentUseCase
) : ViewModel() {
    private val _bookingState = MutableStateFlow(BookingUiState())
    val bookingState: StateFlow<BookingUiState> = _bookingState.asStateFlow()
    
    fun selectTickets(eventId: String, ticketSelection: TicketSelection)
    fun processPayment(paymentMethod: PaymentMethod)
    fun confirmBooking()
    fun cancelBooking(bookingId: String)
    fun getBookingHistory()
}

data class BookingUiState(
    val selectedEvent: Event? = null,
    val ticketSelection: TicketSelection? = null,
    val paymentMethod: PaymentMethod? = null,
    val bookingHistory: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun TicketSelectionScreen(
    event: Event,
    onTicketsSelected: (TicketSelection) -> Unit
)

@Composable
fun PaymentScreen(
    booking: Booking,
    onPaymentComplete: (PaymentResult) -> Unit
)

@Composable
fun BookingConfirmationScreen(
    booking: Booking,
    onViewTickets: () -> Unit
)
```

**Dependencies**: `shared/domain`, `shared/ui-common`, `shared/data`

### feature/social
**Purpose**: Friends, sharing, and social event features

**Responsibilities**:
- Friend management
- Social sharing
- Group coordination
- Social activity feed
- Event discussions

**Key Components**:
```kotlin
class SocialViewModel(
    private val socialUseCase: SocialUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _socialState = MutableStateFlow(SocialUiState())
    val socialState: StateFlow<SocialUiState> = _socialState.asStateFlow()
    
    fun loadFriendsList()
    fun getFriendsAttending(eventId: String)
    fun followUser(userId: String)
    fun shareEvent(eventId: String, platform: SharePlatform)
    fun loadSocialFeed()
}

data class SocialUiState(
    val friends: List<User> = emptyList(),
    val friendsAttending: Map<String, List<User>> = emptyMap(),
    val socialFeed: List<SocialActivity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun FriendsListScreen(
    viewModel: SocialViewModel = hiltViewModel(),
    onFriendClick: (String) -> Unit
)

@Composable
fun SocialFeedScreen(
    viewModel: SocialViewModel = hiltViewModel(),
    onActivityClick: (SocialActivity) -> Unit
)

@Composable
fun EventSharingScreen(
    event: Event,
    onShare: (SharePlatform) -> Unit
)
```

**Dependencies**: `shared/domain`, `shared/ui-common`, `shared/data`

### feature/tickets
**Purpose**: Digital ticket wallet, QR codes, and offline access

**Responsibilities**:
- Digital ticket storage
- QR code generation
- Offline ticket access
- Ticket transfer
- Ticket validation

**Key Components**:
```kotlin
class TicketWalletViewModel(
    private val ticketRepository: TicketRepository,
    private val qrCodeGenerator: QRCodeGenerator
) : ViewModel() {
    private val _ticketState = MutableStateFlow(TicketWalletUiState())
    val ticketState: StateFlow<TicketWalletUiState> = _ticketState.asStateFlow()
    
    fun loadActiveTickets()
    fun generateQRCode(ticketId: String)
    fun downloadTicket(ticketId: String)
    fun transferTicket(ticketId: String, recipientId: String)
    fun validateTicket(ticketId: String)
}

data class TicketWalletUiState(
    val activeTickets: List<Ticket> = emptyList(),
    val qrCodes: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun TicketWalletScreen(
    viewModel: TicketWalletViewModel = hiltViewModel(),
    onTicketClick: (String) -> Unit
)

@Composable
fun TicketDetailScreen(
    ticket: Ticket,
    qrCode: String,
    onDownload: () -> Unit,
    onTransfer: () -> Unit
)

@Composable
fun QRCodeScanner(
    onQRCodeScanned: (String) -> Unit
)
```

**Dependencies**: `shared/domain`, `shared/ui-common`, `shared/data`

### feature/notifications
**Purpose**: Event reminders, updates, and friend activity

**Responsibilities**:
- Push notification management
- Event reminders
- Friend activity alerts
- Price drop notifications
- Custom notification preferences

**Key Components**:
```kotlin
class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {
    private val _notificationState = MutableStateFlow(NotificationUiState())
    val notificationState: StateFlow<NotificationUiState> = _notificationState.asStateFlow()
    
    fun loadNotifications()
    fun markAsRead(notificationId: String)
    fun updateNotificationPreferences(preferences: NotificationPreferences)
    fun scheduleEventReminder(eventId: String, reminderTime: Instant)
    fun cancelEventReminder(eventId: String)
}

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val preferences: NotificationPreferences? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    onNotificationClick: (Notification) -> Unit
)

@Composable
fun NotificationPreferencesScreen(
    preferences: NotificationPreferences,
    onPreferencesChanged: (NotificationPreferences) -> Unit
)
```

**Dependencies**: `shared/domain`, `shared/ui-common`, `shared/data`

## 📱 Platform-Specific User Experience

### platform/android
**Purpose**: Android-specific user interactions and experiences

**Responsibilities**:
- Material Design 3 implementation
- Android-specific notifications
- Android navigation patterns
- Android permissions handling
- Android-specific integrations

**Key Components**:
```kotlin
// Android-specific UI components
@Composable
fun AndroidEventCard(
    event: Event,
    onEventClick: (String) -> Unit,
    onWishlistClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        // Material Design 3 implementation
    }
}

// Android notification manager
class AndroidNotificationManager(
    private val context: Context
) : NotificationManager {
    override fun showEventReminder(event: Event, reminderTime: Instant) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Event Reminder")
            .setContentText("Your event '${event.title}' starts soon")
            .setSmallIcon(R.drawable.ic_event)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(event.id.hashCode(), notification)
    }
}

// Android permissions handler
class AndroidPermissionsHandler(
    private val activity: ComponentActivity
) : PermissionsHandler {
    override fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
}
```

**Dependencies**: `shared/ui-common`, `shared/domain`

### platform/ios
**Purpose**: iOS-specific user interactions and experiences

**Responsibilities**:
- Cupertino design implementation
- iOS-specific notifications
- iOS navigation patterns
- iOS permissions handling
- iOS-specific integrations

**Key Components**:
```kotlin
// iOS-specific UI components
@Composable
fun iOSEventCard(
    event: Event,
    onEventClick: (String) -> Unit,
    onWishlistClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CupertinoColors.systemBackground)
    ) {
        // Cupertino design implementation
    }
}

// iOS notification manager
class iOSNotificationManager(
    private val notificationCenter: UNUserNotificationCenter
) : NotificationManager {
    override fun showEventReminder(event: Event, reminderTime: Instant) {
        val content = UNMutableNotificationContent().apply {
            title = "Event Reminder"
            body = "Your event '${event.title}' starts soon"
            sound = UNNotificationSound.default
        }
        
        val trigger = UNTimeIntervalNotificationTrigger(
            timeInterval = reminderTime.epochSeconds - System.currentTimeMillis() / 1000,
            repeats = false
        )
        
        val request = UNNotificationRequest(
            identifier = "event_${event.id}",
            content = content,
            trigger = trigger
        )
        
        notificationCenter.add(request)
    }
}

// iOS permissions handler
class iOSPermissionsHandler(
    private val locationManager: CLLocationManager
) : PermissionsHandler {
    override fun requestLocationPermission() {
        when (locationManager.authorizationStatus) {
            CLAuthorizationStatus.notDetermined -> {
                locationManager.requestWhenInUseAuthorization()
            }
            CLAuthorizationStatus.denied, CLAuthorizationStatus.restricted -> {
                // Handle denied permissions
            }
            else -> {
                // Permission already granted
            }
        }
    }
}
```

**Dependencies**: `shared/ui-common`, `shared/domain`

## 🔄 Module Communication Patterns

### User State Management
```kotlin
// Global user state management
class UserStateManager(
    private val userSessionManager: UserSessionManager,
    private val userPreferencesUseCase: UserPreferencesUseCase
) {
    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()
    
    fun updateUserState(user: User) {
        _userState.value = UserState.Authenticated(user)
        userSessionManager.updateUserSession(user)
    }
    
    fun clearUserState() {
        _userState.value = UserState.Unauthenticated
        userSessionManager.clearUserSession()
    }
}

sealed class UserState {
    object Loading : UserState()
    object Unauthenticated : UserState()
    data class Authenticated(val user: User) : UserState()
}
```

### Event Data Sharing
```kotlin
// Event data sharing between discovery and booking
class EventDataManager(
    private val eventRepository: EventRepository,
    private val bookingRepository: BookingRepository
) {
    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()
    
    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }
    
    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }
    
    fun getEventWithBookingInfo(eventId: String): Flow<EventWithBookingInfo> {
        return combine(
            eventRepository.getEventDetails(eventId),
            bookingRepository.getActiveTickets(eventId)
        ) { event, tickets ->
            EventWithBookingInfo(event, tickets)
        }
    }
}
```

### Social Data Integration
```kotlin
// Social data integration across features
class SocialDataManager(
    private val socialRepository: SocialRepository,
    private val userRepository: UserRepository
) {
    private val _friendsList = MutableStateFlow<List<User>>(emptyList())
    val friendsList: StateFlow<List<User>> = _friendsList.asStateFlow()
    
    fun loadFriendsList(userId: String) {
        viewModelScope.launch {
            _friendsList.value = socialRepository.getFriendsList(userId)
        }
    }
    
    fun getFriendsAttending(eventId: String): Flow<List<User>> {
        return socialRepository.getFriendsAttending(eventId)
    }
    
    fun updateSocialActivity(activity: SocialActivity) {
        // Update social feed across all features
    }
}
```

### Offline-First Architecture
```kotlin
// Offline-first data synchronization
class OfflineDataManager(
    private val database: AppDatabase,
    private val networkApi: NetworkApi
) {
    suspend fun syncUserData(userId: String) {
        try {
            // Sync user profile
            val remoteProfile = networkApi.getUserProfile(userId)
            database.userProfileDao().insertUserProfile(remoteProfile.toEntity())
            
            // Sync events
            val remoteEvents = networkApi.getUserEvents(userId)
            database.eventDao().insertEvents(remoteEvents.map { it.toEntity() })
            
            // Sync tickets
            val remoteTickets = networkApi.getUserTickets(userId)
            database.ticketDao().insertTickets(remoteTickets.map { it.toEntity() })
        } catch (e: Exception) {
            // Handle offline mode - data already cached locally
        }
    }
    
    suspend fun queueOfflineAction(action: OfflineAction) {
        database.offlineActionDao().insertAction(action)
    }
    
    suspend fun processOfflineActions() {
        val actions = database.offlineActionDao().getPendingActions()
        actions.forEach { action ->
            try {
                when (action.type) {
                    OfflineActionType.BOOKING -> processBookingAction(action)
                    OfflineActionType.REVIEW -> processReviewAction(action)
                    OfflineActionType.SOCIAL -> processSocialAction(action)
                }
                database.offlineActionDao().markActionCompleted(action.id)
            } catch (e: Exception) {
                // Keep action in queue for retry
            }
        }
    }
}
```

## 📊 Dependency Graph (User Data Flow)

```
app/
├── feature/discovery
│   ├── shared/domain (EventDiscoveryUseCase)
│   ├── shared/data (EventRepository)
│   ├── shared/ui-common (EventCard, SearchBar)
│   └── shared/database (EventEntity, EventDao)
│
├── feature/booking
│   ├── shared/domain (BookingUseCase, PaymentUseCase)
│   ├── shared/data (BookingRepository)
│   ├── shared/ui-common (BookingSummaryCard)
│   └── shared/database (BookingEntity, TicketEntity)
│
├── feature/social
│   ├── shared/domain (SocialUseCase)
│   ├── shared/data (SocialRepository)
│   ├── shared/ui-common (UserProfileCard)
│   └── shared/database (SocialConnectionEntity)
│
├── feature/tickets
│   ├── shared/domain (TicketUseCase)
│   ├── shared/data (TicketRepository)
│   ├── shared/ui-common (TicketCard, QRCode)
│   └── shared/database (TicketEntity)
│
└── shared/
    ├── core (UserSessionManager, AppConfig)
    ├── domain (All Use Cases)
    ├── data (All Repositories)
    ├── network (All API Clients)
    ├── database (All Entities and DAOs)
    └── ui-common (All UI Components)
```

## 🔄 Cross-Feature User Interactions

### Event Discovery → Booking Flow
```kotlin
// Navigation from discovery to booking
@Composable
fun EventDiscoveryScreen(
    onEventClick: (String) -> Unit,
    onBookNowClick: (String) -> Unit
) {
    // Event discovery UI
    EventCard(
        event = event,
        onEventClick = onEventClick,
        onBookNowClick = onBookNowClick
    )
}

// Shared event data between features
class EventBookingCoordinator(
    private val eventDataManager: EventDataManager,
    private val bookingViewModel: BookingViewModel
) {
    fun startBookingFlow(eventId: String) {
        eventDataManager.selectEvent(eventId)
        // Navigate to booking screen
    }
}
```

### Social → Discovery Integration
```kotlin
// Social features integrated into discovery
@Composable
fun EventCardWithSocial(
    event: Event,
    friendsAttending: List<User>,
    onEventClick: (String) -> Unit,
    onFriendClick: (String) -> Unit
) {
    EventCard(event = event, onEventClick = onEventClick)
    FriendsAttendingSection(
        friends = friendsAttending,
        onFriendClick = onFriendClick
    )
}
```

### Notifications → Feature Navigation
```kotlin
// Notification handling across features
class NotificationHandler(
    private val navigationManager: NavigationManager,
    private val eventDataManager: EventDataManager
) {
    fun handleNotification(notification: Notification) {
        when (notification.type) {
            NotificationType.EVENT_REMINDER -> {
                eventDataManager.selectEvent(notification.eventId)
                navigationManager.navigateToTickets()
            }
            NotificationType.FRIEND_ACTIVITY -> {
                navigationManager.navigateToSocial()
            }
            NotificationType.PRICE_DROP -> {
                eventDataManager.selectEvent(notification.eventId)
                navigationManager.navigateToBooking()
            }
        }
    }
}
```

This user-centric multi-module architecture provides a comprehensive foundation for building an engaging event discovery and booking app that prioritizes user experience, offline capabilities, and seamless cross-feature interactions.

