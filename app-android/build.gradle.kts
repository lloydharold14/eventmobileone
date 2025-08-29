plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(project(":composeApp"))
            implementation(project(":core:ui"))
            implementation(project(":core:designsystem"))
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.preview)
        }
    }
}

android {
    namespace = "com.eventsmobileone.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.eventsmobileone"
        minSdk = 24
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        
        // Build config fields for environment configuration
        buildConfigField("String", "ENVIRONMENT", "\"dev\"")
        buildConfigField("String", "API_BASE_URL", "\"https://dev-api.event.tkhtech.com\"")
        buildConfigField("boolean", "IS_DEBUG", "true")
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "ENVIRONMENT", "\"dev\"")
            buildConfigField("String", "API_BASE_URL", "\"https://dev-api.event.tkhtech.com\"")
            buildConfigField("boolean", "IS_DEBUG", "true")
            buildConfigField("String", "VERSION_NAME", "\"1.0.0-dev\"")
            buildConfigField("int", "VERSION_CODE", "1")
        }
        
        release {
            isMinifyEnabled = false
            buildConfigField("String", "ENVIRONMENT", "\"prod\"")
            buildConfigField("String", "API_BASE_URL", "\"https://api.event.tkhtech.com\"")
            buildConfigField("boolean", "IS_DEBUG", "false")
            buildConfigField("String", "VERSION_NAME", "\"1.0.0\"")
            buildConfigField("int", "VERSION_CODE", "1")
        }
    }
    
    // Build variants for different environments
    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "ENVIRONMENT", "\"dev\"")
            buildConfigField("String", "API_BASE_URL", "\"https://dev-api.event.tkhtech.com\"")
            buildConfigField("boolean", "IS_DEBUG", "true")
        }
        
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "ENVIRONMENT", "\"staging\"")
            buildConfigField("String", "API_BASE_URL", "\"https://staging-api.event.tkhtech.com\"")
            buildConfigField("boolean", "IS_DEBUG", "true")
        }
        
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "ENVIRONMENT", "\"prod\"")
            buildConfigField("String", "API_BASE_URL", "\"https://api.event.tkhtech.com\"")
            buildConfigField("boolean", "IS_DEBUG", "false")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    lint {
        disable += "NullSafeMutableLiveData"
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
