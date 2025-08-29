# 🏗️ Build Variants & Environment Configuration

## Overview

This project follows Kotlin API guidelines for **testability** and **maintainability** by implementing different build variants for different environments. This ensures that each environment has its own configuration without hardcoded URLs.

## 🎯 Build Variants

### **Available Variants:**

| Variant | Environment | API URL | Debug | Application ID |
|---------|-------------|---------|-------|----------------|
| `devDebug` | Development | `https://dev-api.event.tkhtech.com` | ✅ | `com.eventsmobileone.dev` |
| `devRelease` | Development | `https://dev-api.event.tkhtech.com` | ❌ | `com.eventsmobileone.dev` |
| `stagingDebug` | Staging | `https://staging-api.event.tkhtech.com` | ✅ | `com.eventsmobileone.staging` |
| `stagingRelease` | Staging | `https://staging-api.event.tkhtech.com` | ❌ | `com.eventsmobileone.staging` |
| `prodDebug` | Production | `https://api.event.tkhtech.com` | ✅ | `com.eventsmobileone` |
| `prodRelease` | Production | `https://api.event.tkhtech.com` | ❌ | `com.eventsmobileone` |

## 🚀 Building Different Variants

### **Development Build:**
```bash
# Debug version for development
./gradlew :app-android:assembleDevDebug

# Release version for development testing
./gradlew :app-android:assembleDevRelease
```

### **Staging Build:**
```bash
# Debug version for staging
./gradlew :app-android:assembleStagingDebug

# Release version for staging
./gradlew :app-android:assembleStagingRelease
```

### **Production Build:**
```bash
# Debug version for production testing
./gradlew :app-android:assembleProdDebug

# Release version for production
./gradlew :app-android:assembleProdRelease
```

## 📱 Installing Different Variants

### **Development:**
```bash
./gradlew :app-android:installDevDebug
```

### **Staging:**
```bash
./gradlew :app-android:installStagingDebug
```

### **Production:**
```bash
./gradlew :app-android:installProdDebug
```

## 🔧 Configuration

### **Build Configuration:**

The build configuration is defined in:
- `app-android/build.gradle.kts` - Android app configuration
- `core/data/build.gradle.kts` - Core data module configuration

### **Environment-Specific Settings:**

Each variant automatically configures:
- **API Base URL**: Environment-specific API endpoints
- **Application ID**: Unique package name for each environment
- **Version Name**: Environment-specific version naming
- **Debug Flags**: Environment-specific debug settings

### **Build Config Fields:**

```kotlin
// Generated at build time for each variant
buildConfigField("String", "ENVIRONMENT", "\"dev\"")
buildConfigField("String", "API_BASE_URL", "\"https://dev-api.event.tkhtech.com\"")
buildConfigField("boolean", "IS_DEBUG", "true")
```

## 🧪 Testing

### **Testing Different Environments:**

1. **Build the variant** you want to test:
   ```bash
   ./gradlew :app-android:assembleDevDebug
   ```

2. **Install on device**:
   ```bash
   ./gradlew :app-android:installDevDebug
   ```

3. **Verify the configuration**:
   - Check the app's package name in device settings
   - Verify API calls go to the correct environment
   - Confirm debug features are enabled/disabled as expected

### **Verifying API Endpoints:**

Each variant will make API calls to its configured endpoint:
- **Dev**: `https://dev-api.event.tkhtech.com`
- **Staging**: `https://staging-api.event.tkhtech.com`
- **Prod**: `https://api.event.tkhtech.com`

## 🔒 Security Benefits

### **No Hardcoded URLs:**
- ✅ URLs are configured at build time
- ✅ Different environments use different URLs
- ✅ No risk of accidentally using production URLs in development

### **Environment Isolation:**
- ✅ Each environment has its own application ID
- ✅ Can install multiple variants simultaneously
- ✅ Clear separation between environments

### **Build-Time Configuration:**
- ✅ Configuration is baked into the APK
- ✅ Cannot be changed at runtime
- ✅ Reduces risk of configuration errors

## 🛠️ Development Workflow

### **Daily Development:**
1. Use `devDebug` variant for development
2. Test with development API endpoints
3. Use debug features and logging

### **Staging Testing:**
1. Use `stagingDebug` variant for testing
2. Test with staging API endpoints
3. Verify production-like behavior

### **Production Release:**
1. Use `prodRelease` variant for production
2. Test with production API endpoints
3. Ensure all debug features are disabled

## 📋 Best Practices

### **Following Kotlin API Guidelines:**

1. **Testability**: Each environment can be tested independently
2. **Maintainability**: Configuration is centralized and version-controlled
3. **Simplicity**: Clear, predictable configuration system
4. **Consistency**: Same configuration approach across all modules

### **Development Guidelines:**

1. **Always use appropriate variant** for your environment
2. **Test all variants** before releasing
3. **Verify API endpoints** are correct for each environment
4. **Use debug variants** for development and testing
5. **Use release variants** for production builds

## 🔍 Troubleshooting

### **Common Issues:**

1. **Wrong API endpoint**: Verify you're using the correct variant
2. **Build errors**: Clean and rebuild the project
3. **Installation conflicts**: Uninstall previous variants first

### **Debug Commands:**

```bash
# Clean all builds
./gradlew clean

# List all available tasks
./gradlew tasks

# Build specific variant with info
./gradlew :app-android:assembleDevDebug --info
```

---

**This configuration ensures that your app follows best practices for environment management and maintains the highest standards of security and maintainability.** 🎉
