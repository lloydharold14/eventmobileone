# Platform Configuration for Android and iOS

## Overview

This document outlines the complete platform configuration for the Event Management app, ensuring proper support for both Android and iOS targets using Kotlin Multiplatform (KMP) and Compose Multiplatform.

## 🏗️ Architecture Overview

### Multi-Platform Structure
```
EventApp/
├── composeApp/                    # Main app entry point
│   ├── androidMain/              # Android-specific code
│   ├── iosMain/                  # iOS-specific code
│   └── commonMain/               # Shared UI code
├── platform/                     # Platform implementations
│   ├── common/                   # Platform-agnostic interfaces
│   ├── android/                  # Android-specific implementations
│   └── ios/                      # iOS-specific implementations
└── iosApp/                       # iOS Xcode project
    ├── iosApp.xcodeproj/         # Xcode project
    ├── Podfile                   # CocoaPods configuration
    └── iosApp/                   # iOS app source
```

## 📱 Platform-Specific Configurations

### Android Configuration

#### Build Configuration (`composeApp/build.gradle.kts`)
```kotlin
androidTarget {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

android {
    namespace = "com.tech.tkh.eventapp"
    compileSdk = 36
    minSdk = 24
    targetSdk = 35
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "2.2.0"
    }
}
```

#### Android-Specific Dependencies
- **Activity Compose**: `androidx.activity:activity-compose`
- **Lifecycle**: `androidx.lifecycle:lifecycle-viewmodel`
- **Biometric**: `androidx.biometric:biometric`
- **Work Manager**: `androidx.work:work-runtime-ktx`
- **DataStore**: `androidx.datastore:datastore-preferences`

#### Android Platform Services
- **Network**: OkHttp + Ktor Android client
- **Database**: Room + SQLDelight Android driver
- **Storage**: SharedPreferences + DataStore
- **Location**: LocationManager
- **Notifications**: NotificationManager + WorkManager
- **Biometric**: BiometricPrompt + BiometricManager

### iOS Configuration

#### Build Configuration (`composeApp/build.gradle.kts`)
```kotlin
listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
).forEach { iosTarget ->
    iosTarget.binaries.framework {
        baseName = "EventApp"
        isStatic = true
        export(project(":core:domain"))
        export(project(":core:data"))
        export(project(":ui:core"))
        export(project(":ui:theme"))
        export(project(":ui:components"))
        export(project(":ui:navigation"))
    }
}
```

#### iOS-Specific Dependencies
- **Koin Core**: Dependency injection
- **Platform Services**: iOS-specific implementations

#### iOS Platform Services
- **Network**: URLSession + Ktor iOS client
- **Database**: Core Data + SQLDelight iOS driver
- **Storage**: NSUserDefaults
- **Location**: CoreLocation
- **Notifications**: UNUserNotificationCenter
- **Biometric**: LocalAuthentication

## 🔧 Platform Abstraction Layer

### Common Interfaces (`platform:common`)

#### Platform Detection
```kotlin
expect class Platform() {
    val platform: String
    val version: String
    val isDebug: Boolean
}
```

#### Network Client
```kotlin
expect class NetworkClient() {
    suspend fun get(url: String): String
    suspend fun post(url: String, data: String): String
}
```

#### Database
```kotlin
expect class Database() {
    suspend fun <T> query(sql: String, mapper: (Map<String, Any?>) -> T): List<T>
    suspend fun execute(sql: String, params: Map<String, Any?> = emptyMap())
    suspend fun close()
}
```

#### Key-Value Storage
```kotlin
expect class KeyValueStorage() {
    suspend fun getString(key: String, defaultValue: String = ""): String
    suspend fun putString(key: String, value: String)
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun remove(key: String)
    suspend fun clear()
}
```

#### Location Service
```kotlin
expect class LocationService() {
    suspend fun getCurrentLocation(): Location?
    suspend fun requestLocationPermission(): Boolean
    fun observeLocationUpdates(): Flow<Location>
}
```

#### Notification Service
```kotlin
expect class NotificationService() {
    suspend fun showNotification(title: String, message: String)
    suspend fun requestPermission(): Boolean
    suspend fun scheduleNotification(id: String, title: String, message: String, delayMillis: Long)
    suspend fun cancelNotification(id: String)
}
```

#### Biometric Authentication
```kotlin
expect class BiometricAuth() {
    suspend fun authenticate(reason: String): Boolean
    suspend fun isAvailable(): Boolean
}
```

## 🛠️ Platform Implementations

### Android Implementation (`platform:android`)

#### Platform Detection
```kotlin
actual class Platform actual constructor() {
    actual val platform: String = "Android ${Build.VERSION.RELEASE}"
    actual val version: String = Build.VERSION.SDK_INT.toString()
    actual val isDebug: Boolean = BuildConfig.DEBUG
}
```

#### Key-Value Storage (SharedPreferences)
```kotlin
actual class KeyValueStorage actual constructor() {
    actual suspend fun getString(key: String, defaultValue: String): String {
        // Android SharedPreferences implementation
        return defaultValue
    }
    
    actual suspend fun putString(key: String, value: String) {
        // Android SharedPreferences implementation
    }
}
```

### iOS Implementation (`platform:ios`)

#### Platform Detection
```kotlin
actual class Platform actual constructor() {
    actual val platform: String = "iOS ${UIDevice.currentDevice.systemVersion}"
    actual val version: String = UIDevice.currentDevice.systemVersion
    actual val isDebug: Boolean = NSProcessInfo.processInfo.environment["DEBUG"] != null
}
```

#### Key-Value Storage (NSUserDefaults)
```kotlin
actual class KeyValueStorage actual constructor() {
    private val defaults = NSUserDefaults.standardUserDefaults
    
    actual suspend fun getString(key: String, defaultValue: String): String {
        return defaults.objectForKey(key) as? String ?: defaultValue
    }
    
    actual suspend fun putString(key: String, value: String) {
        defaults.setObject(value, key)
    }
}
```

## 📦 iOS Integration

### CocoaPods Configuration (`iosApp/Podfile`)
```ruby
target 'iosApp' do
  use_frameworks!
  platform :ios, '14.0'
  pod 'EventApp', :path => '../'
end
```

### SwiftUI Integration (`iosApp/iosApp/ContentView.swift`)
```swift
import SwiftUI
import EventApp

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
```

## ⚙️ Build Configuration

### Gradle Properties (`gradle.properties`)
```properties
# Kotlin Multiplatform
kotlin.mpp.enableGranularSourceSetsMetadata=true
kotlin.mpp.androidSourceSetLayoutVersion=2
kotlin.native.enableDependencyPropagation=false
kotlin.native.binary.memoryModel=experimental
kotlin.native.cocoapods.generate.wrapper=true
kotlin.native.cocoapods.platform=ios
kotlin.native.cocoapods.deploymentTarget=14.0

# iOS
ios.deploymentTarget=14.0
ios.frameworkName=EventApp
ios.frameworkType=static

# Compose
org.jetbrains.compose.experimental.uikit.enabled=true
```

### Module Dependencies

#### Feature Modules
- **Android**: Android-specific dependencies (Activity, Lifecycle, etc.)
- **iOS**: iOS-specific dependencies (Koin core)
- **Common**: Shared dependencies (Compose, Koin, Navigation)

#### Core Modules
- **Domain**: Pure Kotlin (no platform dependencies)
- **Data**: Platform-agnostic with platform-specific implementations
- **DI**: Platform-specific DI modules
- **Common**: Shared utilities

## 🧪 Testing Configuration

### Android Testing
```kotlin
androidTest {
    dependencies {
        implementation(libs.androidx.testRunner)
        implementation(libs.androidx.testExt.junit)
        implementation(libs.androidx.espresso.core)
    }
}
```

### iOS Testing
```kotlin
iosTest {
    dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlin.testJunit)
    }
}
```

### Common Testing
```kotlin
commonTest {
    dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlin.testJunit)
        implementation(libs.coroutines.test)
        implementation(libs.mockk)
        implementation(libs.turbine)
    }
}
```

## 🚀 Development Workflow

### Android Development
1. Open project in Android Studio
2. Sync Gradle dependencies
3. Run on Android device/emulator
4. Use Android-specific debugging tools

### iOS Development
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Run `pod install` in `iosApp` directory
3. Build and run on iOS device/simulator
4. Use Xcode debugging tools

### Cross-Platform Development
1. Implement business logic in `core:domain`
2. Create platform-agnostic interfaces in `platform:common`
3. Implement platform-specific code in `platform:android` and `platform:ios`
4. Test on both platforms

## 📋 Platform-Specific Considerations

### Android Considerations
- **Permissions**: Handle runtime permissions properly
- **Lifecycle**: Respect Android activity lifecycle
- **Background**: Use WorkManager for background tasks
- **UI**: Follow Material Design guidelines
- **Performance**: Optimize for Android memory management

### iOS Considerations
- **Permissions**: Request permissions at appropriate times
- **Lifecycle**: Handle iOS app lifecycle events
- **Background**: Use background app refresh appropriately
- **UI**: Follow iOS Human Interface Guidelines
- **Performance**: Optimize for iOS memory management

### Shared Considerations
- **State Management**: Use consistent state management across platforms
- **Navigation**: Implement platform-appropriate navigation patterns
- **Error Handling**: Provide consistent error handling
- **Localization**: Support multiple languages
- **Accessibility**: Ensure accessibility compliance

This configuration ensures that the Event Management app works seamlessly on both Android and iOS platforms while maintaining a clean, maintainable codebase with maximum code sharing.

