@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // ---------------- PROJECT MODULES ----------------
    implementation(project(":core"))
    implementation(project(":core-ui"))
    implementation(project(":feature-home"))
    implementation(project(":feature-report"))
    implementation(project(":feature-map"))
    implementation(project(":feature-profile"))

    // ---------------- COMPOSE ----------------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.icons.extended)

    debugImplementation(libs.androidx.compose.ui.tooling)

    // ---------------- NAVIGATION ----------------
    implementation(libs.navigation.compose)

    // ---------------- LIFECYCLE ----------------
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // ---------------- COROUTINES ----------------
    implementation(libs.kotlinx.coroutines.core)

    // ---------------- ANDROID CORE ----------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // ---------------- MAPS ----------------
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    implementation(libs.play.services.location)
}
