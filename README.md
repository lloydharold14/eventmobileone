# EventMO - Kotlin Multiplatform Event Management App

This is a Kotlin Multiplatform project targeting Android, iOS, and Desktop with a multi-module architecture for scalable development.

## 🏗️ Multi-Module Architecture

The project has been restructured into a clean multi-module baseline:

### Core Modules
- **`:core:model`** - Pure Kotlin data models, interfaces, and value classes
- **`:core:ui`** - Shared Compose Multiplatform UI components and screens  
- **`:core:designsystem`** - Theme, colors, typography, and design tokens

### Application Module
- **`:app-android`** - Android application launcher with minimal platform-specific code

### Legacy Module (To be migrated)
- **`:composeApp`** - Original template module (will be phased out)

## 🚀 Running the App

### Android
```bash
./gradlew :app-android:assembleDebug
```

The Android app uses the new `:app-android` module which depends on the shared core modules.

### Module Dependencies
```
:app-android
├── :core:ui
├── :core:designsystem
└── :core:model (via :core:ui)
```

## 📂 Module Structure

```
EventMO/
├── app-android/              # Android application launcher
│   ├── src/androidMain/      # Android-specific code
│   └── src/main/res/         # Android resources
├── core/
│   ├── model/               # Pure Kotlin models
│   ├── ui/                  # Shared UI components
│   └── designsystem/        # Theme and design tokens
├── composeApp/              # Legacy module (to be migrated)
└── iosApp/                  # iOS application entry point
```

## 🛠️ Architecture Principles

- **Clean Architecture**: Clear separation between modules
- **Kotlin 2.x**: Latest Kotlin with JDK 17 support  
- **Compose Multiplatform**: Shared UI components across platforms
- **No Circular Dependencies**: Enforced module dependency rules

## 🎯 Next Steps

This baseline enables further development of:
- Feature modules (events, auth, booking, etc.)
- Data layer with repositories
- Navigation system
- Dependency injection
- Platform-specific implementations

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…