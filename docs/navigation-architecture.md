# Navigation Architecture
## User-Centric Navigation for Event Discovery & Booking App

## Overview
This document outlines a comprehensive navigation architecture for the multi-module KMP event discovery app, designed around user journeys and intuitive navigation patterns. The architecture prioritizes seamless user experience, deep linking capabilities, and cross-module state management.

## 🧭 Navigation Architecture Principles

### User Journey-First Design
- **Flow-Based Navigation**: Navigation follows natural user journeys
- **Context Preservation**: User state maintained across navigation
- **Progressive Disclosure**: Information revealed as needed
- **Gesture-Friendly**: Intuitive touch and gesture navigation

### Consumer App Patterns
- **Bottom Navigation**: Primary user functions easily accessible
- **Tab-Based Browsing**: Organized content discovery
- **Modal Flows**: Focused task completion
- **Deep Linking**: Seamless external app integration

## 🗺️ User Journey Navigation Flows

### 1. Onboarding and User Registration Flow

```kotlin
// Navigation flow: Welcome → Onboarding → Registration → Preferences → Home
sealed class OnboardingDestination(val route: String) {
    object Welcome : OnboardingDestination("welcome")
    object Onboarding : OnboardingDestination("onboarding")
    object Registration : OnboardingDestination("registration")
    object Preferences : OnboardingDestination("preferences")
    object Home : OnboardingDestination("home")
}

// Onboarding navigation coordinator
class OnboardingNavigationCoordinator(
    private val navController: NavController,
    private val userStateManager: UserStateManager
) {
    fun startOnboardingFlow() {
        navController.navigate(OnboardingDestination.Welcome.route) {
            popUpTo(0) { inclusive = true }
        }
    }
    
    fun completeOnboarding(user: User) {
        userStateManager.updateUserState(user)
        navController.navigate(OnboardingDestination.Home.route) {
            popUpTo(0) { inclusive = true }
        }
    }
    
    fun skipOnboarding() {
        navController.navigate(OnboardingDestination.Home.route) {
            popUpTo(0) { inclusive = true }
        }
    }
}

// Onboarding screens with navigation
@Composable
fun OnboardingFlow(
    navController: NavController,
    userStateManager: UserStateManager
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingDestination.Welcome.route
    ) {
        composable(OnboardingDestination.Welcome.route) {
            WelcomeScreen(
                onGetStarted = { navController.navigate(OnboardingDestination.Onboarding.route) },
                onSkip = { skipOnboarding() }
            )
        }
        
        composable(OnboardingDestination.Onboarding.route) {
            OnboardingScreen(
                onComplete = { navController.navigate(OnboardingDestination.Registration.route) }
            )
        }
        
        composable(OnboardingDestination.Registration.route) {
            RegistrationScreen(
                onRegistrationComplete = { user ->
                    navController.navigate(OnboardingDestination.Preferences.route)
                }
            )
        }
        
        composable(OnboardingDestination.Preferences.route) {
            PreferencesScreen(
                onPreferencesComplete = { preferences ->
                    completeOnboarding(user)
                }
            )
        }
    }
}
```

### 2. Event Discovery to Booking Completion Flow

```kotlin
// Navigation flow: Discovery → Event Details → Ticket Selection → Payment → Confirmation
sealed class EventBookingDestination(val route: String) {
    object Discovery : EventBookingDestination("discovery")
    object EventDetails : EventBookingDestination("event/{eventId}") {
        fun createRoute(eventId: String) = "event/$eventId"
    }
    object TicketSelection : EventBookingDestination("ticket-selection/{eventId}") {
        fun createRoute(eventId: String) = "ticket-selection/$eventId"
    }
    object Payment : EventBookingDestination("payment/{bookingId}") {
        fun createRoute(bookingId: String) = "payment/$bookingId"
    }
    object BookingConfirmation : EventBookingDestination("confirmation/{bookingId}") {
        fun createRoute(bookingId: String) = "confirmation/$bookingId"
    }
}

// Event booking navigation coordinator
class EventBookingNavigationCoordinator(
    private val navController: NavController,
    private val eventDataManager: EventDataManager
) {
    fun navigateToEventDetails(eventId: String) {
        eventDataManager.selectEvent(eventId)
        navController.navigate(EventBookingDestination.EventDetails.createRoute(eventId))
    }
    
    fun startBookingFlow(eventId: String) {
        navController.navigate(EventBookingDestination.TicketSelection.createRoute(eventId))
    }
    
    fun proceedToPayment(bookingId: String) {
        navController.navigate(EventBookingDestination.Payment.createRoute(bookingId))
    }
    
    fun completeBooking(bookingId: String) {
        navController.navigate(EventBookingDestination.BookingConfirmation.createRoute(bookingId)) {
            popUpTo(EventBookingDestination.Discovery.route) { inclusive = false }
        }
    }
}

// Event booking flow implementation
@Composable
fun EventBookingFlow(
    navController: NavController,
    eventDataManager: EventDataManager
) {
    NavHost(
        navController = navController,
        startDestination = EventBookingDestination.Discovery.route
    ) {
        composable(EventBookingDestination.Discovery.route) {
            EventDiscoveryScreen(
                onEventClick = { eventId ->
                    navigateToEventDetails(eventId)
                },
                onBookNowClick = { eventId ->
                    startBookingFlow(eventId)
                }
            )
        }
        
        composable(
            route = EventBookingDestination.EventDetails.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            EventDetailsScreen(
                eventId = eventId,
                onBookNow = { startBookingFlow(eventId) },
                onShare = { shareEvent(eventId) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = EventBookingDestination.TicketSelection.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            TicketSelectionScreen(
                eventId = eventId,
                onTicketsSelected = { bookingId ->
                    proceedToPayment(bookingId)
                },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = EventBookingDestination.Payment.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: return@composable
            PaymentScreen(
                bookingId = bookingId,
                onPaymentComplete = { completeBooking(bookingId) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = EventBookingDestination.BookingConfirmation.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: return@composable
            BookingConfirmationScreen(
                bookingId = bookingId,
                onViewTickets = { navController.navigate("tickets") },
                onContinueBrowsing = { navController.navigate(EventBookingDestination.Discovery.route) }
            )
        }
    }
}
```

### 3. Social Sharing and Friend Invitation Flows

```kotlin
// Navigation flow: Event Details → Share Options → Social Platform → Invite Friends
sealed class SocialSharingDestination(val route: String) {
    object ShareOptions : SocialSharingDestination("share-options/{eventId}") {
        fun createRoute(eventId: String) = "share-options/$eventId"
    }
    object FriendInvitation : SocialSharingDestination("invite-friends/{eventId}") {
        fun createRoute(eventId: String) = "invite-friends/$eventId"
    }
    object SocialFeed : SocialSharingDestination("social-feed")
    object FriendProfile : SocialSharingDestination("friend/{userId}") {
        fun createRoute(userId: String) = "friend/$userId"
    }
}

// Social sharing navigation coordinator
class SocialSharingNavigationCoordinator(
    private val navController: NavController,
    private val socialDataManager: SocialDataManager
) {
    fun showShareOptions(eventId: String) {
        navController.navigate(SocialSharingDestination.ShareOptions.createRoute(eventId))
    }
    
    fun inviteFriends(eventId: String) {
        navController.navigate(SocialSharingDestination.FriendInvitation.createRoute(eventId))
    }
    
    fun navigateToSocialFeed() {
        navController.navigate(SocialSharingDestination.SocialFeed.route)
    }
    
    fun viewFriendProfile(userId: String) {
        navController.navigate(SocialSharingDestination.FriendProfile.createRoute(userId))
    }
}

// Social sharing flow implementation
@Composable
fun SocialSharingFlow(
    navController: NavController,
    socialDataManager: SocialDataManager
) {
    NavHost(
        navController = navController,
        startDestination = SocialSharingDestination.SocialFeed.route
    ) {
        composable(SocialSharingDestination.SocialFeed.route) {
            SocialFeedScreen(
                onActivityClick = { activity ->
                    when (activity.type) {
                        SocialActivityType.EVENT_JOINED -> {
                            navController.navigate("event/${activity.eventId}")
                        }
                        SocialActivityType.FRIEND_ACTIVITY -> {
                            viewFriendProfile(activity.userId)
                        }
                    }
                },
                onShareEvent = { eventId ->
                    showShareOptions(eventId)
                }
            )
        }
        
        composable(
            route = SocialSharingDestination.ShareOptions.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            ShareOptionsScreen(
                eventId = eventId,
                onShareToSocial = { platform ->
                    shareToSocialPlatform(eventId, platform)
                    navController.popBackStack()
                },
                onInviteFriends = { inviteFriends(eventId) },
                onCopyLink = { copyEventLink(eventId) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = SocialSharingDestination.FriendInvitation.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            FriendInvitationScreen(
                eventId = eventId,
                onInviteSelected = { friendIds ->
                    sendInvitations(eventId, friendIds)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
```

### 4. User Profile and Settings Management

```kotlin
// Navigation flow: Profile → Settings → Specific Settings → Back to Profile
sealed class ProfileDestination(val route: String) {
    object Profile : ProfileDestination("profile")
    object Settings : ProfileDestination("settings")
    object Preferences : ProfileDestination("preferences")
    object Privacy : ProfileDestination("privacy")
    object Notifications : ProfileDestination("notifications")
    object BookingHistory : ProfileDestination("booking-history")
    object EditProfile : ProfileDestination("edit-profile")
}

// Profile navigation coordinator
class ProfileNavigationCoordinator(
    private val navController: NavController,
    private val userStateManager: UserStateManager
) {
    fun navigateToSettings() {
        navController.navigate(ProfileDestination.Settings.route)
    }
    
    fun navigateToPreferences() {
        navController.navigate(ProfileDestination.Preferences.route)
    }
    
    fun navigateToBookingHistory() {
        navController.navigate(ProfileDestination.BookingHistory.route)
    }
    
    fun editProfile() {
        navController.navigate(ProfileDestination.EditProfile.route)
    }
    
    fun signOut() {
        userStateManager.clearUserState()
        navController.navigate("auth") {
            popUpTo(0) { inclusive = true }
        }
    }
}

// Profile navigation implementation
@Composable
fun ProfileFlow(
    navController: NavController,
    userStateManager: UserStateManager
) {
    NavHost(
        navController = navController,
        startDestination = ProfileDestination.Profile.route
    ) {
        composable(ProfileDestination.Profile.route) {
            ProfileScreen(
                onEditProfile = { editProfile() },
                onSettings = { navigateToSettings() },
                onBookingHistory = { navigateToBookingHistory() },
                onSignOut = { signOut() }
            )
        }
        
        composable(ProfileDestination.Settings.route) {
            SettingsScreen(
                onPreferences = { navigateToPreferences() },
                onPrivacy = { navController.navigate(ProfileDestination.Privacy.route) },
                onNotifications = { navController.navigate(ProfileDestination.Notifications.route) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(ProfileDestination.Preferences.route) {
            PreferencesScreen(
                onPreferencesChanged = { preferences ->
                    updateUserPreferences(preferences)
                },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(ProfileDestination.BookingHistory.route) {
            BookingHistoryScreen(
                onBookingClick = { bookingId ->
                    navController.navigate("booking/$bookingId")
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
```

### 5. Ticket Access and QR Code Display Flow

```kotlin
// Navigation flow: Tickets → Ticket Details → QR Code → Share Ticket
sealed class TicketDestination(val route: String) {
    object TicketWallet : TicketDestination("tickets")
    object TicketDetails : TicketDestination("ticket/{ticketId}") {
        fun createRoute(ticketId: String) = "ticket/$ticketId"
    }
    object QRCodeDisplay : TicketDestination("qr-code/{ticketId}") {
        fun createRoute(ticketId: String) = "qr-code/$ticketId"
    }
    object TicketTransfer : TicketDestination("transfer/{ticketId}") {
        fun createRoute(ticketId: String) = "transfer/$ticketId"
    }
}

// Ticket navigation coordinator
class TicketNavigationCoordinator(
    private val navController: NavController,
    private val ticketDataManager: TicketDataManager
) {
    fun viewTicketDetails(ticketId: String) {
        navController.navigate(TicketDestination.TicketDetails.createRoute(ticketId))
    }
    
    fun showQRCode(ticketId: String) {
        navController.navigate(TicketDestination.QRCodeDisplay.createRoute(ticketId))
    }
    
    fun transferTicket(ticketId: String) {
        navController.navigate(TicketDestination.TicketTransfer.createRoute(ticketId))
    }
    
    fun downloadTicket(ticketId: String) {
        ticketDataManager.downloadTicket(ticketId)
    }
}

// Ticket flow implementation
@Composable
fun TicketFlow(
    navController: NavController,
    ticketDataManager: TicketDataManager
) {
    NavHost(
        navController = navController,
        startDestination = TicketDestination.TicketWallet.route
    ) {
        composable(TicketDestination.TicketWallet.route) {
            TicketWalletScreen(
                onTicketClick = { ticketId ->
                    viewTicketDetails(ticketId)
                },
                onQRCodeClick = { ticketId ->
                    showQRCode(ticketId)
                },
                onTransferClick = { ticketId ->
                    transferTicket(ticketId)
                }
            )
        }
        
        composable(
            route = TicketDestination.TicketDetails.route,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: return@composable
            TicketDetailsScreen(
                ticketId = ticketId,
                onShowQRCode = { showQRCode(ticketId) },
                onTransfer = { transferTicket(ticketId) },
                onDownload = { downloadTicket(ticketId) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = TicketDestination.QRCodeDisplay.route,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: return@composable
            QRCodeDisplayScreen(
                ticketId = ticketId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
```

## 🎯 User-Centric Navigation Features

### Deep Linking for Event Sharing

```kotlin
// Deep link handling for event sharing
class DeepLinkHandler(
    private val navController: NavController,
    private val eventDataManager: EventDataManager
) {
    fun handleDeepLink(uri: Uri) {
        when (uri.host) {
            "event" -> {
                val eventId = uri.lastPathSegment
                if (eventId != null) {
                    eventDataManager.selectEvent(eventId)
                    navController.navigate("event/$eventId")
                }
            }
            "ticket" -> {
                val ticketId = uri.lastPathSegment
                if (ticketId != null) {
                    navController.navigate("ticket/$ticketId")
                }
            }
            "invite" -> {
                val inviteCode = uri.lastPathSegment
                if (inviteCode != null) {
                    handleInviteCode(inviteCode)
                }
            }
        }
    }
    
    private fun handleInviteCode(inviteCode: String) {
        // Handle friend invitation codes
        viewModelScope.launch {
            val inviteData = socialRepository.getInviteData(inviteCode)
            when (inviteData.type) {
                InviteType.EVENT -> navController.navigate("event/${inviteData.eventId}")
                InviteType.FRIEND -> navController.navigate("friend/${inviteData.userId}")
            }
        }
    }
}

// Deep link configuration
@Composable
fun AppNavigation(
    navController: NavController,
    deepLinkHandler: DeepLinkHandler
) {
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        // Handle incoming deep links
        context.registerActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let { uri ->
                deepLinkHandler.handleDeepLink(uri)
            }
        }
    }
    
    NavHost(navController = navController, startDestination = "main") {
        // Navigation graph
    }
}
```

### Social Media Integration

```kotlin
// Social media sharing integration
class SocialMediaSharing(
    private val context: Context,
    private val eventRepository: EventRepository
) {
    fun shareToSocialMedia(eventId: String, platform: SocialPlatform) {
        viewModelScope.launch {
            val event = eventRepository.getEventDetails(eventId)
            val shareUrl = generateShareUrl(eventId)
            
            when (platform) {
                SocialPlatform.FACEBOOK -> shareToFacebook(event, shareUrl)
                SocialPlatform.TWITTER -> shareToTwitter(event, shareUrl)
                SocialPlatform.INSTAGRAM -> shareToInstagram(event, shareUrl)
                SocialPlatform.WHATSAPP -> shareToWhatsApp(event, shareUrl)
            }
        }
    }
    
    private fun shareToFacebook(event: Event, shareUrl: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this event: ${event.title}\n$shareUrl")
            setPackage("com.facebook.katana")
        }
        context.startActivity(intent)
    }
    
    private fun shareToTwitter(event: Event, shareUrl: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this event: ${event.title}\n$shareUrl")
            setPackage("com.twitter.android")
        }
        context.startActivity(intent)
    }
}
```

### Calendar Integration

```kotlin
// Calendar integration for adding events
class CalendarIntegration(
    private val context: Context,
    private val eventRepository: EventRepository
) {
    fun addEventToCalendar(eventId: String) {
        viewModelScope.launch {
            val event = eventRepository.getEventDetails(eventId)
            
            val intent = Intent(Intent.ACTION_INSERT).apply {
                type = "vnd.android.cursor.item/event"
                putExtra(CalendarContract.Events.TITLE, event.title)
                putExtra(CalendarContract.Events.DESCRIPTION, event.description)
                putExtra(CalendarContract.Events.EVENT_LOCATION, event.venue.name)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.startDateTime.toEpochMilli())
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.endDateTime.toEpochMilli())
                putExtra(CalendarContract.Events.ALL_DAY, false)
            }
            
            context.startActivity(intent)
        }
    }
    
    fun checkCalendarPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun requestCalendarPermission() {
        // Request calendar permission
    }
}
```

### Map Integration

```kotlin
// Map integration for event location navigation
class MapIntegration(
    private val context: Context,
    private val eventRepository: EventRepository
) {
    fun navigateToEventLocation(eventId: String) {
        viewModelScope.launch {
            val event = eventRepository.getEventDetails(eventId)
            val venue = event.venue
            
            val uri = Uri.parse("geo:${venue.latitude},${venue.longitude}?q=${venue.name}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                // Fallback to generic map app
                val fallbackUri = Uri.parse("geo:${venue.latitude},${venue.longitude}")
                val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
                context.startActivity(fallbackIntent)
            }
        }
    }
    
    fun showEventOnMap(eventId: String) {
        viewModelScope.launch {
            val event = eventRepository.getEventDetails(eventId)
            val venue = event.venue
            
            // Open custom map view with event location
            navController.navigate("map?lat=${venue.latitude}&lng=${venue.longitude}&title=${venue.name}")
        }
    }
}
```

## 🎨 User Experience Navigation Patterns

### Bottom Navigation for Main User Functions

```kotlin
// Bottom navigation implementation
@Composable
fun MainBottomNavigation(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Discover") },
            label = { Text("Discover") },
            selected = currentRoute == "discovery",
            onClick = {
                navController.navigate("discovery") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "Tickets") },
            label = { Text("Tickets") },
            selected = currentRoute == "tickets",
            onClick = {
                navController.navigate("tickets") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = "Social") },
            label = { Text("Social") },
            selected = currentRoute == "social",
            onClick = {
                navController.navigate("social") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = {
                navController.navigate("profile") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }
}
```

### Tab-Based Browsing Within Event Discovery

```kotlin
// Tab-based event discovery
@Composable
fun EventDiscoveryTabs(
    currentTab: DiscoveryTab,
    onTabSelected: (DiscoveryTab) -> Unit
) {
    TabRow(selectedTabIndex = currentTab.ordinal) {
        DiscoveryTab.values().forEach { tab ->
            Tab(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                text = { Text(tab.title) },
                icon = { Icon(tab.icon, contentDescription = tab.title) }
            )
        }
    }
}

enum class DiscoveryTab(
    val title: String,
    val icon: ImageVector
) {
    RECOMMENDED("Recommended", Icons.Default.Star),
    TRENDING("Trending", Icons.Default.TrendingUp),
    NEARBY("Nearby", Icons.Default.LocationOn),
    CATEGORIES("Categories", Icons.Default.Category),
    WISHLIST("Wishlist", Icons.Default.Favorite)
}

@Composable
fun EventDiscoveryScreen(
    viewModel: EventDiscoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        EventDiscoveryTabs(
            currentTab = uiState.currentTab,
            onTabSelected = { viewModel.selectTab(it) }
        )
        
        when (uiState.currentTab) {
            DiscoveryTab.RECOMMENDED -> RecommendedEventsList(uiState.recommendedEvents)
            DiscoveryTab.TRENDING -> TrendingEventsList(uiState.trendingEvents)
            DiscoveryTab.NEARBY -> NearbyEventsList(uiState.nearbyEvents)
            DiscoveryTab.CATEGORIES -> EventCategoriesList(uiState.categories)
            DiscoveryTab.WISHLIST -> WishlistEventsList(uiState.wishlistEvents)
        }
    }
}
```

### Modal Flows for Booking and Payment

```kotlin
// Modal booking flow
@Composable
fun ModalBookingFlow(
    eventId: String,
    onDismiss: () -> Unit,
    onBookingComplete: (String) -> Unit
) {
    var currentStep by remember { mutableStateOf(BookingStep.TICKET_SELECTION) }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        when (currentStep) {
            BookingStep.TICKET_SELECTION -> {
                TicketSelectionModal(
                    eventId = eventId,
                    onTicketsSelected = { bookingId ->
                        currentStep = BookingStep.PAYMENT
                    },
                    onDismiss = onDismiss
                )
            }
            BookingStep.PAYMENT -> {
                PaymentModal(
                    bookingId = bookingId,
                    onPaymentComplete = { bookingId ->
                        currentStep = BookingStep.CONFIRMATION
                    },
                    onBack = { currentStep = BookingStep.TICKET_SELECTION }
                )
            }
            BookingStep.CONFIRMATION -> {
                BookingConfirmationModal(
                    bookingId = bookingId,
                    onViewTickets = { onBookingComplete(bookingId) },
                    onContinueBrowsing = onDismiss
                )
            }
        }
    }
}

enum class BookingStep {
    TICKET_SELECTION,
    PAYMENT,
    CONFIRMATION
}
```

### Slide-Up Panels for Event Details

```kotlin
// Slide-up event details panel
@Composable
fun EventDetailsSlideUp(
    eventId: String,
    onDismiss: () -> Unit,
    onBookNow: () -> Unit
) {
    val event by viewModel.getEvent(eventId).collectAsState()
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Event header
            EventHeader(event = event)
            
            // Event details
            EventDetailsContent(event = event)
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onBookNow,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Book Now")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedButton(
                    onClick = { /* Share event */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Share")
                }
            }
        }
    }
}
```

### Gesture-Based Navigation for Image Browsing

```kotlin
// Gesture-based image browsing
@Composable
fun EventImageGallery(
    images: List<String>,
    onImageClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var currentImageIndex by remember { mutableStateOf(0) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Image viewer with gestures
        ImageViewer(
            images = images,
            currentIndex = currentImageIndex,
            onIndexChanged = { currentImageIndex = it },
            onSwipeToDismiss = onDismiss
        )
        
        // Close button
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
        
        // Image counter
        Text(
            text = "${currentImageIndex + 1}/${images.size}",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
fun ImageViewer(
    images: List<String>,
    currentIndex: Int,
    onIndexChanged: (Int) -> Unit,
    onSwipeToDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    
    LaunchedEffect(currentIndex) {
        pagerState.animateScrollToPage(currentIndex)
    }
    
    LaunchedEffect(pagerState.currentPage) {
        onIndexChanged(pagerState.currentPage)
    }
    
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        AsyncImage(
            model = images[page],
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}
```

## 🔄 Cross-Module User State Management

### Maintain User Session Across Feature Modules

```kotlin
// Global user session management
class UserSessionManager(
    private val userRepository: UserRepository,
    private val secureStorage: SecureStorage
) {
    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSession: StateFlow<UserSession?> = _userSession.asStateFlow()
    
    suspend fun initializeSession() {
        val savedSession = secureStorage.getUserSession()
        if (savedSession != null && !savedSession.isExpired()) {
            _userSession.value = savedSession
        }
    }
    
    suspend fun updateSession(user: User) {
        val session = UserSession(
            user = user,
            token = generateToken(user),
            expiresAt = Instant.now().plus(Duration.ofDays(30))
        )
        secureStorage.saveUserSession(session)
        _userSession.value = session
    }
    
    suspend fun clearSession() {
        secureStorage.clearUserSession()
        _userSession.value = null
    }
    
    fun isAuthenticated(): Boolean {
        return userSession.value != null && !userSession.value!!.isExpired()
    }
}

data class UserSession(
    val user: User,
    val token: String,
    val expiresAt: Instant
) {
    fun isExpired(): Boolean = Instant.now().isAfter(expiresAt)
}
```

### Preserve User Search and Filter Preferences

```kotlin
// User search and filter preferences
class UserPreferencesManager(
    private val dataStore: DataStore<Preferences>
) {
    private val _searchPreferences = MutableStateFlow(SearchPreferences())
    val searchPreferences: StateFlow<SearchPreferences> = _searchPreferences.asStateFlow()
    
    suspend fun updateSearchPreferences(preferences: SearchPreferences) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.LAST_SEARCH_QUERY] = preferences.query
            prefs[PreferencesKeys.LAST_SEARCH_LOCATION] = preferences.location
            prefs[PreferencesKeys.LAST_SEARCH_CATEGORIES] = preferences.categories.joinToString(",")
            prefs[PreferencesKeys.LAST_SEARCH_DATE_RANGE] = preferences.dateRange.toString()
        }
        _searchPreferences.value = preferences
    }
    
    suspend fun loadSearchPreferences() {
        dataStore.data.collect { prefs ->
            val preferences = SearchPreferences(
                query = prefs[PreferencesKeys.LAST_SEARCH_QUERY] ?: "",
                location = prefs[PreferencesKeys.LAST_SEARCH_LOCATION] ?: "",
                categories = prefs[PreferencesKeys.LAST_SEARCH_CATEGORIES]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList(),
                dateRange = prefs[PreferencesKeys.LAST_SEARCH_DATE_RANGE]?.let { DateRange.valueOf(it) } ?: DateRange.ALL
            )
            _searchPreferences.value = preferences
        }
    }
}

data class SearchPreferences(
    val query: String = "",
    val location: String = "",
    val categories: List<String> = emptyList(),
    val dateRange: DateRange = DateRange.ALL,
    val priceRange: PriceRange = PriceRange.ALL
)
```

### Handle Authentication State in Navigation

```kotlin
// Authentication-aware navigation
class AuthenticationAwareNavigation(
    private val navController: NavController,
    private val userSessionManager: UserSessionManager
) {
    fun navigateToAuthenticatedRoute(route: String) {
        if (userSessionManager.isAuthenticated()) {
            navController.navigate(route)
        } else {
            navController.navigate("auth") {
                popUpTo(0) { inclusive = false }
            }
        }
    }
    
    fun handleAuthenticationResult(success: Boolean) {
        if (success) {
            navController.navigate("main") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    fun requireAuthentication(route: String) {
        if (!userSessionManager.isAuthenticated()) {
            navController.navigate("auth") {
                popUpTo(0) { inclusive = false }
            }
        }
    }
}

// Authentication guard for protected routes
@Composable
fun AuthenticationGuard(
    userSession: UserSession?,
    onAuthenticated: @Composable () -> Unit,
    onUnauthenticated: @Composable () -> Unit
) {
    if (userSession != null && !userSession.isExpired()) {
        onAuthenticated()
    } else {
        onUnauthenticated()
    }
}
```

### Manage User's Current Booking State Across Modules

```kotlin
// Global booking state management
class BookingStateManager(
    private val bookingRepository: BookingRepository
) {
    private val _currentBooking = MutableStateFlow<Booking?>(null)
    val currentBooking: StateFlow<Booking?> = _currentBooking.asStateFlow()
    
    private val _bookingHistory = MutableStateFlow<List<Booking>>(emptyList())
    val bookingHistory: StateFlow<List<Booking>> = _bookingHistory.asStateFlow()
    
    suspend fun startBooking(eventId: String) {
        val booking = Booking(
            id = generateBookingId(),
            eventId = eventId,
            status = BookingStatus.DRAFT,
            createdAt = Instant.now()
        )
        _currentBooking.value = booking
    }
    
    suspend fun updateBooking(booking: Booking) {
        _currentBooking.value = booking
    }
    
    suspend fun completeBooking(booking: Booking) {
        val completedBooking = booking.copy(
            status = BookingStatus.CONFIRMED,
            completedAt = Instant.now()
        )
        bookingRepository.saveBooking(completedBooking)
        _currentBooking.value = null
        loadBookingHistory()
    }
    
    suspend fun cancelBooking(bookingId: String) {
        bookingRepository.cancelBooking(bookingId)
        if (_currentBooking.value?.id == bookingId) {
            _currentBooking.value = null
        }
        loadBookingHistory()
    }
    
    private suspend fun loadBookingHistory() {
        val history = bookingRepository.getBookingHistory()
        _bookingHistory.value = history
    }
}

enum class BookingStatus {
    DRAFT,
    PENDING_PAYMENT,
    CONFIRMED,
    CANCELLED,
    REFUNDED
}
```

## 🚀 Implementation Summary

This navigation architecture provides:

### **User Journey Optimization**
- **Seamless Flows**: Natural progression through user tasks
- **Context Preservation**: User state maintained across navigation
- **Deep Linking**: External app integration for sharing
- **Gesture Support**: Intuitive touch navigation

### **Consumer App Patterns**
- **Bottom Navigation**: Easy access to main functions
- **Tab-Based Browsing**: Organized content discovery
- **Modal Flows**: Focused task completion
- **Slide-Up Panels**: Quick information access

### **Cross-Module Integration**
- **Global State Management**: User session across features
- **Preference Persistence**: Search and filter preferences
- **Authentication Guards**: Protected route handling
- **Booking State**: Seamless booking flow management

### **Platform Integration**
- **Social Media Sharing**: Direct platform integration
- **Calendar Integration**: Event scheduling
- **Map Integration**: Location navigation
- **Deep Link Handling**: External app communication

This architecture ensures a smooth, intuitive user experience that follows modern consumer app patterns while maintaining clean separation between modules and robust state management across the entire application.

