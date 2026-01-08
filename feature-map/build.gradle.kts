plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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
    // compiler is provided via composeOptions/resolutionStrategy

    implementation(project(":core"))
    implementation(project(":core-ui"))

    // --- Compose ---
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.icons.extended)

    // --- Navigation (only if NavHost is used) ---
    implementation(libs.navigation.compose)

    // --- Lifecycle ---
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // --- Coroutines ---
    implementation(libs.kotlinx.coroutines.core)

    // --- AndroidX ---
    implementation(libs.androidx.core.ktx)

    // --- Google Maps (MapView) ---
    implementation(libs.play.services.maps) {
        exclude(group = "com.google.maps.android", module = "maps-compose")
    }
    implementation(libs.play.services.location)

    // --- Debug ---
    debugImplementation(libs.androidx.compose.ui.tooling)
}
