plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.sinaliza.coreui"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
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
    // Compose BOM + UI
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")

    // Icons extended (for Map etc.)
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Navigation for Compose (important — fixes your unresolved references)
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // lifecycle (optional)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // helpful tooling (inspector)
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")

    // core ktx
    implementation("androidx.core:core-ktx:1.10.1")
}
