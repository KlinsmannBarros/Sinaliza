@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.sinaliza"
    compileSdk {
        version = release(36)
    }

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

    // Enable Compose
    buildFeatures {
        compose = true
    }

    // Compiler extension version for Compose — keep this synced with Compose UI version
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // Core modules
    implementation(project(":core"))
    implementation(project(":core-ui"))

    // Feature modules
    implementation(project(":feature-home"))
    implementation(project(":feature-report"))
    implementation(project(":feature-map"))
    implementation(project(":feature-profile"))

    // existing libs from version catalog
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Compose basics (explicit versions are safe fallbacks if not in catalog)
    implementation("androidx.activity:activity-compose:1.12.0")
    implementation("androidx.compose.ui:ui:1.9.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.9.5")
    implementation("androidx.compose.material3:material3:1.4.0")

    // Navigation Compose (using your version-catalog alias)
    implementation(libs.navigation.compose)

    // Optional helpful extras (debug tooling & icons)
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.5")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.9.5")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // tests (keep your existing aliases)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
