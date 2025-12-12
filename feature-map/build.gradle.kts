plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.sinaliza.feature.map"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // MUST match app module
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core"))

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))

    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Required for Map icons (Place, MyLocation, ChevronRight)
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation (needed if MapRoute() uses NavController)
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // Lifecycle / ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    // AndroidX core
    implementation("androidx.core:core-ktx:1.17.0")

    // Google Maps & Location
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.maps.android:maps-compose:6.12.2")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
}
