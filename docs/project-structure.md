# Project Structure Overview

## 🏗️ Multi-Module Architecture

This document outlines the complete project structure for the Event Management app built with Compose Multiplatform.

## 📁 Directory Structure

```
EventApp/
├── composeApp/                    # Main app entry point
│   ├── androidMain/              # Android-specific code
│   ├── iosMain/                  # iOS-specific code
│   ├── commonMain/               # Shared UI entry point
│   └── build.gradle.kts
├── feature/                      # Feature modules
│   ├── events/                   # Event discovery & management
│   ├── auth/                     # Authentication
│   ├── bookings/                 # Ticket booking
│   ├── profile/                  # User profile
│   ├── notifications/            # Push notifications
│   ├── search/                   # Event search
│   ├── maps/                     # Location services
│   ├── organizer/                # Organizer dashboard
│   └── */build.gradle.kts        # Feature module configs
├── ui/                          # Shared UI components
│   ├── core/                    # Core UI components
│   ├── theme/                   # Design system
│   ├── components/              # Reusable components
│   ├── navigation/              # Navigation components
│   └── */build.gradle.kts       # UI module configs
├── core/                        # Business logic
│   ├── domain/                  # Entities & use cases
│   ├── data/                    # Data sources
│   ├── di/                      # Dependency injection
│   ├── common/                  # Shared utilities
│   └── */build.gradle.kts       # Core module configs
├── platform/                    # Platform implementations
│   ├── android/                 # Android-specific code
│   ├── ios/                     # iOS-specific code
│   ├── common/                  # Platform interfaces
│   └── */build.gradle.kts       # Platform module configs
├── data/                        # Data layer
│   ├── local/                   # Local database
│   ├── remote/                  # API clients
│   ├── repository/              # Repository implementations
│   └── */build.gradle.kts       # Data module configs
├── gradle/                      # Gradle configuration
│   ├── libs.versions.toml       # Dependency versions
│   └── wrapper/                 # Gradle wrapper
├── docs/                        # Documentation
├── build.gradle.kts             # Root build config
├── settings.gradle.kts          # Project settings
├── gradle.properties            # Gradle properties
├── .gitignore                   # Git ignore rules
└── README.md                    # Project README
```

## 🔧 Module Dependencies

### Dependency Graph
```
composeApp
├── feature:events
├── feature:auth
├── feature:bookings
├── feature:profile
├── feature:notifications
├── feature:search
├── feature:maps
├── feature:organizer
├── ui:core
├── ui:theme
├── ui:components
├── ui:navigation
└── core:di

feature:*
├── core:domain
├── core:data
├── ui:core
├── ui:theme
├── ui:components
├── ui:navigation
└── platform:common

ui:*
├── core:common
└── compose dependencies

core:data
├── core:domain
├── platform:common
├── ktor
└── sqldelight

core:di
├── core:domain
├── core:data
├── platform:common
└── koin

platform:*
└── kotlinx.coroutines

data:*
├── core:domain
├── platform:common
└── specific data dependencies
```

## 📦 Module Details

### App Layer (`composeApp/`)
- **Purpose**: Main application entry point
- **Dependencies**: All feature modules, UI modules, core:di
- **Responsibilities**: App initialization, navigation orchestration

### Feature Modules (`feature/`)
- **Purpose**: Business features with UI
- **Structure**: Each feature contains presentation, domain, and DI layers
- **Dependencies**: Core modules, UI modules, platform:common
- **Examples**: events, auth, bookings, profile, notifications, search, maps, organizer

### UI Layer (`ui/`)
- **Purpose**: Shared UI components and design system
- **Structure**: Core components, theme, reusable components, navigation
- **Dependencies**: Core:common, Compose Multiplatform
- **Reusability**: Used across all feature modules

### Core Layer (`core/`)
- **Purpose**: Business logic and data management
- **Structure**: Domain (entities/use cases), Data (repositories), DI, Common (utilities)
- **Dependencies**: Platform modules, external libraries
- **Testability**: Pure Kotlin, easily testable

### Platform Layer (`platform/`)
- **Purpose**: Platform-specific implementations
- **Structure**: Android, iOS, Common interfaces
- **Dependencies**: Kotlin standard library only
- **Abstraction**: Platform-agnostic interfaces

### Data Layer (`data/`)
- **Purpose**: Data persistence and remote communication
- **Structure**: Local (database), Remote (API), Repository (coordination)
- **Dependencies**: Core:domain, platform:common
- **Technologies**: SQLDelight, Ktor, Room (Android)

## 🛠️ Build Configuration

### Root `build.gradle.kts`
- Plugin management for all modules
- Common repository configuration
- Kotlin compiler options
- Opt-in annotations

### `gradle/libs.versions.toml`
- Centralized dependency version management
- Latest stable versions of all libraries
- Organized by category (Kotlin, Compose, AndroidX, etc.)

### Module `build.gradle.kts`
- KMP configuration with Android and iOS targets
- Module-specific dependencies
- Compose configuration for UI modules
- Testing setup

## 🎯 Key Features

### Multi-Platform Support
- **Android**: Full Android support with Material Design 3
- **iOS**: Native iOS support with Cupertino design
- **Shared Code**: Maximum code sharing between platforms

### Modern Architecture
- **Clean Architecture**: Clear separation of concerns
- **MVVM**: ViewModels for state management
- **Dependency Injection**: Koin for DI
- **Reactive Programming**: Kotlin Coroutines and Flow

### Development Experience
- **Type Safety**: Kotlin with strict typing
- **IDE Support**: Full IntelliJ IDEA support
- **Hot Reload**: Compose Multiplatform hot reload
- **Testing**: Comprehensive testing setup

### Performance
- **Lazy Loading**: Modules loaded on demand
- **Efficient Compilation**: Incremental compilation
- **Memory Management**: Proper resource disposal
- **Network Optimization**: Efficient API communication

## 🚀 Getting Started

### Prerequisites
- Kotlin 2.2.0+
- Android Studio Hedgehog+
- Xcode 15.0+ (for iOS development)
- JDK 17+

### Setup
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on target platform

### Development Workflow
1. **Feature Development**: Work in feature modules
2. **UI Components**: Create reusable components in ui modules
3. **Business Logic**: Implement in core modules
4. **Platform Code**: Add platform-specific code in platform modules
5. **Testing**: Write tests for each layer

## 📋 Module Creation Checklist

### For New Features:
- [ ] Create feature module structure
- [ ] Add build.gradle.kts with dependencies
- [ ] Implement ViewModel with state management
- [ ] Create UI screens with Compose
- [ ] Add platform-specific implementations
- [ ] Configure dependency injection
- [ ] Write unit tests
- [ ] Add UI tests

### For New UI Components:
- [ ] Create component in appropriate ui module
- [ ] Add Compose dependencies
- [ ] Implement platform adaptations
- [ ] Write component tests
- [ ] Document component API

### For New Core Functionality:
- [ ] Define entities in core:domain
- [ ] Implement use cases
- [ ] Create repository interfaces
- [ ] Add data implementations
- [ ] Configure DI modules
- [ ] Write comprehensive tests

This structure provides a solid foundation for building a scalable, maintainable event management app with excellent developer experience and cross-platform support.

