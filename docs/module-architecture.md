# Multi-Module Architecture for Compose Multiplatform Event App

## Architecture Overview

This document outlines a clean, scalable multi-module architecture for the Event Management app built with Compose Multiplatform, following Clean Architecture principles and dependency inversion.

## 🏗️ Module Dependency Graph

```
┌─────────────────────────────────────────────────────────────────┐
│                        APP LAYER                                │
├─────────────────────────────────────────────────────────────────┤
│  composeApp/ (Main App Entry Point)                            │
│  ├── androidMain/ (Android-specific code)                      │
│  ├── iosMain/ (iOS-specific code)                              │
│  └── commonMain/ (Shared UI entry point)                       │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      FEATURE LAYER                              │
├─────────────────────────────────────────────────────────────────┤
│  feature/                                                       │
│  ├── events/           │  ├── auth/           │  ├── profile/   │
│  ├── bookings/         │  ├── notifications/  │  └── organizer/ │
│  └── search/           │  └── maps/           │  └── analytics/ │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                         │
├─────────────────────────────────────────────────────────────────┤
│  ui/                                                             │
│  ├── core/ (Core UI components)                                 │
│  ├── theme/ (Design system)                                     │
│  ├── components/ (Reusable components)                          │
│  └── navigation/ (Navigation components)                        │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                        DOMAIN LAYER                             │
├─────────────────────────────────────────────────────────────────┤
│  core/                                                           │
│  ├── domain/ (Use cases, entities, repositories)                │
│  ├── data/ (Data sources, repositories impl)                    │
│  ├── di/ (Dependency injection)                                 │
│  └── common/ (Shared utilities)                                 │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      PLATFORM LAYER                             │
├─────────────────────────────────────────────────────────────────┤
│  platform/                                                       │
│  ├── android/ (Android-specific implementations)                │
│  ├── ios/ (iOS-specific implementations)                        │
│  └── common/ (Platform-agnostic interfaces)                     │
└─────────────────────────────────────────────────────────────────┘
```

## 📦 Detailed Module Specifications

### 1. App Layer (`composeApp/`)

**Purpose**: Main application entry point and platform-specific configurations

**Structure**:
```
composeApp/
├── androidMain/
│   ├── AndroidManifest.xml
│   ├── MainActivity.kt
│   └── Platform.android.kt
├── iosMain/
│   ├── MainViewController.kt
│   └── Platform.ios.kt
├── commonMain/
│   ├── App.kt
│   ├── MainView.kt
│   └── Platform.kt
└── build.gradle.kts
```

**Dependencies**:
- All feature modules
- `ui:core`
- `ui:theme`
- `ui:navigation`
- `core:di`

**Responsibilities**:
- Application initialization
- Platform-specific setup
- Navigation orchestration
- Dependency injection setup

### 2. Feature Modules (`feature/`)

#### 2.1 Events Module (`feature:events`)

**Purpose**: Event discovery, browsing, and management

**Structure**:
```
feature/events/
├── src/
│   ├── commonMain/
│   │   ├── presentation/
│   │   │   ├── EventsScreen.kt
│   │   │   ├── EventDetailScreen.kt
│   │   │   ├── EventsViewModel.kt
│   │   │   └── EventsState.kt
│   │   ├── domain/
│   │   │   ├── usecase/
│   │   │   │   ├── GetEventsUseCase.kt
│   │   │   │   ├── SearchEventsUseCase.kt
│   │   │   │   └── GetEventDetailsUseCase.kt
│   │   │   └── model/
│   │   │       ├── Event.kt
│   │   │       └── EventFilters.kt
│   │   └── di/
│   │       └── EventsModule.kt
│   ├── androidMain/
│   │   └── EventsPlatform.kt
│   └── iosMain/
│       └── EventsPlatform.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:domain`
- `core:data`
- `ui:core`
- `ui:components`
- `platform:common`

**Public API**:
```kotlin
interface EventsFeature {
    fun createEventsScreen(): @Composable () -> Unit
    fun createEventDetailScreen(eventId: String): @Composable () -> Unit
}
```

#### 2.2 Auth Module (`feature:auth`)

**Purpose**: User authentication and authorization

**Structure**:
```
feature/auth/
├── src/
│   ├── commonMain/
│   │   ├── presentation/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── RegisterScreen.kt
│   │   │   ├── AuthViewModel.kt
│   │   │   └── AuthState.kt
│   │   ├── domain/
│   │   │   ├── usecase/
│   │   │   │   ├── SignInUseCase.kt
│   │   │   │   ├── SignUpUseCase.kt
│   │   │   │   └── SignOutUseCase.kt
│   │   │   └── model/
│   │   │       ├── User.kt
│   │   │       └── AuthCredentials.kt
│   │   └── di/
│   │       └── AuthModule.kt
│   ├── androidMain/
│   │   └── AuthPlatform.kt
│   └── iosMain/
│       └── AuthPlatform.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:domain`
- `core:data`
- `ui:core`
- `platform:common`

#### 2.3 Bookings Module (`feature:bookings`)

**Purpose**: Ticket booking and management

**Structure**:
```
feature/bookings/
├── src/
│   ├── commonMain/
│   │   ├── presentation/
│   │   │   ├── BookingScreen.kt
│   │   │   ├── BookingHistoryScreen.kt
│   │   │   ├── BookingViewModel.kt
│   │   │   └── BookingState.kt
│   │   ├── domain/
│   │   │   ├── usecase/
│   │   │   │   ├── CreateBookingUseCase.kt
│   │   │   │   ├── GetBookingsUseCase.kt
│   │   │   │   └── CancelBookingUseCase.kt
│   │   │   └── model/
│   │   │       ├── Booking.kt
│   │   │       └── Ticket.kt
│   │   └── di/
│   │       └── BookingsModule.kt
│   ├── androidMain/
│   │   └── BookingsPlatform.kt
│   └── iosMain/
│       └── BookingsPlatform.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:domain`
- `core:data`
- `ui:core`
- `ui:components`
- `platform:common`

#### 2.4 Profile Module (`feature:profile`)

**Purpose**: User profile management and preferences

**Structure**:
```
feature/profile/
├── src/
│   ├── commonMain/
│   │   ├── presentation/
│   │   │   ├── ProfileScreen.kt
│   │   │   ├── EditProfileScreen.kt
│   │   │   ├── ProfileViewModel.kt
│   │   │   └── ProfileState.kt
│   │   ├── domain/
│   │   │   ├── usecase/
│   │   │   │   ├── GetProfileUseCase.kt
│   │   │   │   ├── UpdateProfileUseCase.kt
│   │   │   │   └── UpdatePreferencesUseCase.kt
│   │   │   └── model/
│   │   │       ├── UserProfile.kt
│   │   │       └── UserPreferences.kt
│   │   └── di/
│   │       └── ProfileModule.kt
│   ├── androidMain/
│   │   └── ProfilePlatform.kt
│   └── iosMain/
│       └── ProfilePlatform.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:domain`
- `core:data`
- `ui:core`
- `ui:components`
- `platform:common`

#### 2.5 Notifications Module (`feature:notifications`)

**Purpose**: Push notifications and in-app messaging

**Structure**:
```
feature/notifications/
├── src/
│   ├── commonMain/
│   │   ├── presentation/
│   │   │   ├── NotificationsScreen.kt
│   │   │   ├── NotificationsViewModel.kt
│   │   │   └── NotificationsState.kt
│   │   ├── domain/
│   │   │   ├── usecase/
│   │   │   │   ├── GetNotificationsUseCase.kt
│   │   │   │   ├── MarkAsReadUseCase.kt
│   │   │   │   └── UpdatePreferencesUseCase.kt
│   │   │   └── model/
│   │   │       ├── Notification.kt
│   │   │       └── NotificationPreferences.kt
│   │   └── di/
│   │       └── NotificationsModule.kt
│   ├── androidMain/
│   │   └── NotificationsPlatform.kt
│   └── iosMain/
│       └── NotificationsPlatform.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:domain`
- `core:data`
- `ui:core`
- `platform:common`

### 3. UI Layer (`ui/`)

#### 3.1 Core UI (`ui:core`)

**Purpose**: Core UI components and utilities

**Structure**:
```
ui/core/
├── src/
│   ├── commonMain/
│   │   ├── components/
│   │   │   ├── LoadingSpinner.kt
│   │   │   ├── ErrorView.kt
│   │   │   ├── EmptyState.kt
│   │   │   └── RetryButton.kt
│   │   ├── theme/
│   │   │   ├── Color.kt
│   │   │   ├── Typography.kt
│   │   │   └── Shape.kt
│   │   └── utils/
│   │       ├── Extensions.kt
│   │       └── Constants.kt
│   ├── androidMain/
│   │   └── AndroidSpecific.kt
│   └── iosMain/
│       └── IOSSpecific.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:common`

#### 3.2 Theme (`ui:theme`)

**Purpose**: Design system and theming

**Structure**:
```
ui/theme/
├── src/
│   ├── commonMain/
│   │   ├── theme/
│   │   │   ├── AppTheme.kt
│   │   │   ├── ColorScheme.kt
│   │   │   ├── Typography.kt
│   │   │   └── Dimensions.kt
│   │   └── components/
│   │       ├── Button.kt
│   │       ├── TextField.kt
│   │       ├── Card.kt
│   │       └── BottomSheet.kt
│   ├── androidMain/
│   │   └── AndroidTheme.kt
│   └── iosMain/
│       └── IOSTheme.kt
└── build.gradle.kts
```

**Dependencies**:
- `ui:core`

#### 3.3 Components (`ui:components`)

**Purpose**: Reusable UI components

**Structure**:
```
ui/components/
├── src/
│   ├── commonMain/
│   │   ├── event/
│   │   │   ├── EventCard.kt
│   │   │   ├── EventList.kt
│   │   │   └── EventDetail.kt
│   │   ├── booking/
│   │   │   ├── TicketSelector.kt
│   │   │   ├── PaymentForm.kt
│   │   │   └── BookingSummary.kt
│   │   ├── profile/
│   │   │   ├── ProfileHeader.kt
│   │   │   ├── PreferenceItem.kt
│   │   │   └── SettingsList.kt
│   │   └── common/
│   │       ├── SearchBar.kt
│   │       ├── FilterChip.kt
│   │       ├── RatingBar.kt
│   │       └── ImageCarousel.kt
│   ├── androidMain/
│   │   └── AndroidComponents.kt
│   └── iosMain/
│       └── IOSComponents.kt
└── build.gradle.kts
```

**Dependencies**:
- `ui:core`
- `ui:theme`

#### 3.4 Navigation (`ui:navigation`)

**Purpose**: Navigation components and routing

**Structure**:
```
ui/navigation/
├── src/
│   ├── commonMain/
│   │   ├── navigation/
│   │   │   ├── AppNavigator.kt
│   │   │   ├── NavGraph.kt
│   │   │   ├── Screen.kt
│   │   │   └── NavigationState.kt
│   │   └── components/
│   │       ├── BottomNavigation.kt
│   │       ├── TopAppBar.kt
│   │       └── NavigationDrawer.kt
│   ├── androidMain/
│   │   └── AndroidNavigation.kt
│   └── iosMain/
│       └── IOSNavigation.kt
└── build.gradle.kts
```

**Dependencies**:
- `ui:core`
- `ui:theme`

### 4. Core Layer (`core/`)

#### 4.1 Domain (`core:domain`)

**Purpose**: Business logic, entities, and use cases

**Structure**:
```
core/domain/
├── src/
│   ├── commonMain/
│   │   ├── entity/
│   │   │   ├── User.kt
│   │   │   ├── Event.kt
│   │   │   ├── Booking.kt
│   │   │   ├── Ticket.kt
│   │   │   └── Notification.kt
│   │   ├── repository/
│   │   │   ├── UserRepository.kt
│   │   │   ├── EventRepository.kt
│   │   │   ├── BookingRepository.kt
│   │   │   └── NotificationRepository.kt
│   │   ├── usecase/
│   │   │   ├── user/
│   │   │   ├── event/
│   │   │   ├── booking/
│   │   │   └── notification/
│   │   └── model/
│   │       ├── Result.kt
│   │       ├── PagedResult.kt
│   │       └── Error.kt
│   ├── androidMain/
│   │   └── AndroidDomain.kt
│   └── iosMain/
│       └── IOSDomain.kt
└── build.gradle.kts
```

**Dependencies**: None (pure Kotlin)

#### 4.2 Data (`core:data`)

**Purpose**: Data sources and repository implementations

**Structure**:
```
core/data/
├── src/
│   ├── commonMain/
│   │   ├── repository/
│   │   │   ├── UserRepositoryImpl.kt
│   │   │   ├── EventRepositoryImpl.kt
│   │   │   ├── BookingRepositoryImpl.kt
│   │   │   └── NotificationRepositoryImpl.kt
│   │   ├── datasource/
│   │   │   ├── remote/
│   │   │   │   ├── ApiService.kt
│   │   │   │   ├── EventApi.kt
│   │   │   │   └── UserApi.kt
│   │   │   └── local/
│   │   │       ├── Database.kt
│   │   │       ├── EventDao.kt
│   │   │       └── UserDao.kt
│   │   ├── mapper/
│   │   │   ├── EventMapper.kt
│   │   │   ├── UserMapper.kt
│   │   │   └── BookingMapper.kt
│   │   └── model/
│   │       ├── EventEntity.kt
│   │       ├── UserEntity.kt
│   │       └── BookingEntity.kt
│   ├── androidMain/
│   │   └── AndroidData.kt
│   └── iosMain/
│       └── IOSData.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:domain`
- `platform:common`

#### 4.3 DI (`core:di`)

**Purpose**: Dependency injection configuration

**Structure**:
```
core/di/
├── src/
│   ├── commonMain/
│   │   ├── modules/
│   │   │   ├── CoreModule.kt
│   │   │   ├── NetworkModule.kt
│   │   │   ├── DatabaseModule.kt
│   │   │   └── RepositoryModule.kt
│   │   ├── qualifiers/
│   │   │   ├── IoDispatcher.kt
│   │   │   └── DefaultDispatcher.kt
│   │   └── AppModule.kt
│   ├── androidMain/
│   │   └── AndroidModule.kt
│   └── iosMain/
│       └── IOSModule.kt
└── build.gradle.kts
```

**Dependencies**:
- `core:domain`
- `core:data`
- `platform:common`

#### 4.4 Common (`core:common`)

**Purpose**: Shared utilities and extensions

**Structure**:
```
core/common/
├── src/
│   ├── commonMain/
│   │   ├── utils/
│   │   │   ├── DateUtils.kt
│   │   │   ├── StringUtils.kt
│   │   │   ├── ValidationUtils.kt
│   │   │   └── NetworkUtils.kt
│   │   ├── extensions/
│   │   │   ├── StringExtensions.kt
│   │   │   ├── DateExtensions.kt
│   │   │   └── FlowExtensions.kt
│   │   ├── constants/
│   │   │   ├── AppConstants.kt
│   │   │   ├── ApiConstants.kt
│   │   │   └── ValidationConstants.kt
│   │   └── model/
│   │       ├── Resource.kt
│   │       ├── Either.kt
│   │       └── Unit.kt
│   ├── androidMain/
│   │   └── AndroidCommon.kt
│   └── iosMain/
│       └── IOSCommon.kt
└── build.gradle.kts
```

**Dependencies**: None (pure Kotlin)

### 5. Platform Layer (`platform/`)

#### 5.1 Android (`platform:android`)

**Purpose**: Android-specific implementations

**Structure**:
```
platform/android/
├── src/
│   ├── androidMain/
│   │   ├── network/
│   │   │   ├── AndroidNetworkClient.kt
│   │   │   └── AndroidHttpClient.kt
│   │   ├── database/
│   │   │   ├── AndroidDatabase.kt
│   │   │   └── AndroidRoomDatabase.kt
│   │   ├── storage/
│   │   │   ├── AndroidSharedPreferences.kt
│   │   │   └── AndroidFileStorage.kt
│   │   ├── location/
│   │   │   └── AndroidLocationService.kt
│   │   ├── notification/
│   │   │   └── AndroidNotificationService.kt
│   │   └── biometric/
│   │       └── AndroidBiometricAuth.kt
│   └── commonMain/
│       └── AndroidPlatform.kt
└── build.gradle.kts
```

**Dependencies**:
- `platform:common`

#### 5.2 iOS (`platform:ios`)

**Purpose**: iOS-specific implementations

**Structure**:
```
platform/ios/
├── src/
│   ├── iosMain/
│   │   ├── network/
│   │   │   ├── IOSNetworkClient.kt
│   │   │   └── IOSHttpClient.kt
│   │   ├── database/
│   │   │   ├── IOSDatabase.kt
│   │   │   └── IOSCoreDataDatabase.kt
│   │   ├── storage/
│   │   │   ├── IOSUserDefaults.kt
│   │   │   └── IOSFileStorage.kt
│   │   ├── location/
│   │   │   └── IOSLocationService.kt
│   │   ├── notification/
│   │   │   └── IOSNotificationService.kt
│   │   └── biometric/
│   │       └── IOSBiometricAuth.kt
│   └── commonMain/
│       └── IOSPlatform.kt
└── build.gradle.kts
```

**Dependencies**:
- `platform:common`

#### 5.3 Common (`platform:common`)

**Purpose**: Platform-agnostic interfaces

**Structure**:
```
platform/common/
├── src/
│   ├── commonMain/
│   │   ├── network/
│   │   │   ├── NetworkClient.kt
│   │   │   └── HttpClient.kt
│   │   ├── database/
│   │   │   ├── Database.kt
│   │   │   └── DatabaseFactory.kt
│   │   ├── storage/
│   │   │   ├── KeyValueStorage.kt
│   │   │   └── FileStorage.kt
│   │   ├── location/
│   │   │   └── LocationService.kt
│   │   ├── notification/
│   │   │   └── NotificationService.kt
│   │   ├── biometric/
│   │   │   └── BiometricAuth.kt
│   │   └── platform/
│   │       └── Platform.kt
│   ├── androidMain/
│   │   └── AndroidPlatformImpl.kt
│   └── iosMain/
│       └── IOSPlatformImpl.kt
└── build.gradle.kts
```

**Dependencies**: None (pure Kotlin)

## 🔄 Communication Patterns

### 1. Dependency Injection Pattern

```kotlin
// Feature modules expose their dependencies
interface EventsFeature {
    fun createEventsScreen(): @Composable () -> Unit
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

### 2. Event-Driven Communication

```kotlin
// Shared events between modules
sealed class AppEvent {
    data class UserLoggedIn(val user: User) : AppEvent()
    data class EventBooked(val booking: Booking) : AppEvent()
    data class NotificationReceived(val notification: Notification) : AppEvent()
}

// Event bus for cross-module communication
class EventBus {
    private val _events = MutableSharedFlow<AppEvent>()
    val events = _events.asSharedFlow()
    
    suspend fun emit(event: AppEvent) {
        _events.emit(event)
    }
}
```

### 3. Navigation Coordination

```kotlin
// Centralized navigation
class AppNavigator(
    private val eventsFeature: EventsFeature,
    private val authFeature: AuthFeature
) {
    fun navigateToEvent(eventId: String) {
        // Coordinate navigation between features
    }
    
    fun navigateToBooking(bookingId: String) {
        // Handle deep linking
    }
}
```

## 📋 Build Configuration

### Root `build.gradle.kts`
```kotlin
plugins {
    kotlin("multiplatform") version "1.9.0"
    id("org.jetbrains.compose") version "1.5.0"
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
```

### Feature Module `build.gradle.kts`
```kotlin
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:domain"))
                implementation(project(":core:data"))
                implementation(project(":ui:core"))
                implementation(project(":ui:components"))
                implementation(project(":platform:common"))
                
                implementation("org.jetbrains.compose.ui:ui")
                implementation("org.jetbrains.compose.material3:material3")
                implementation("org.jetbrains.compose.ui:ui-tooling-preview")
            }
        }
        
        androidMain {
            dependencies {
                implementation(project(":platform:android"))
            }
        }
        
        iosMain {
            dependencies {
                implementation(project(":platform:ios"))
            }
        }
    }
}
```

## 🎯 Benefits of This Architecture

### 1. **Modularity**
- Clear separation of concerns
- Independent development and testing
- Easy to add/remove features

### 2. **Scalability**
- Horizontal scaling through feature modules
- Vertical scaling through layer separation
- Easy to add new platforms

### 3. **Maintainability**
- Single responsibility principle
- Dependency inversion
- Clear boundaries and contracts

### 4. **Testability**
- Isolated unit testing
- Mocked dependencies
- Feature-level integration testing

### 5. **Reusability**
- Shared business logic
- Platform-agnostic components
- Feature composition

### 6. **Performance**
- Lazy loading of modules
- Efficient dependency resolution
- Platform-specific optimizations

This architecture provides a solid foundation for building a scalable, maintainable event management app with Compose Multiplatform, following clean architecture principles and modern mobile development best practices.

