@file:Suppress("DEPRECATION")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.sinaliza"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sinaliza"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    // MUST match all modules using Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // -------- Project modules --------
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":feature-home"))
    implementation(project(":feature-report"))
    implementation(project(":feature-map"))
    implementation(project(":feature-profile"))

    // -------- Compose BOM --------
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))

    // -------- Compose UI --------
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")   // <-- REQUIRED for weight()
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material Icons (for map buttons)
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // -------- Navigation --------
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // -------- Lifecycle --------
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    // -------- Coroutines --------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    // -------- AndroidX --------
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")

    // -------- Google Maps (Compose + Location) --------
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.maps.android:maps-compose:6.12.2")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // -------- Debug tools --------
    debugImplementation("androidx.compose.ui:ui-tooling")
}
