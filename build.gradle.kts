plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Force a Compose compiler version compatible with the Kotlin version used by the project
configurations.all {
    resolutionStrategy.force("androidx.compose.compiler:compiler:${libs.versions.composeCompiler.get()}")

    // Ensure Kotlin stdlib used in the build matches the Kotlin plugin version to avoid
    // "compiled with an incompatible version of Kotlin" metadata errors when a
    // transitive dependency pulls a newer kotlin-stdlib (e.g., 2.2.x).
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${libs.versions.kotlin.get()}")
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${libs.versions.kotlin.get()}")

    // Force kotlinx-coroutines to the project's declared version to avoid pulls of newer
    // coroutine artifacts compiled with a newer Kotlin metadata version.
    resolutionStrategy.force("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.kotlinxCoroutinesCore.get()}")
    resolutionStrategy.force("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.kotlinxCoroutinesCore.get()}")
}

// Also apply these rules to subprojects (ensures modules inherit the same resolution strategy)
subprojects {
    configurations.all {
        resolutionStrategy.force("androidx.compose.compiler:compiler:${libs.versions.composeCompiler.get()}")
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${libs.versions.kotlin.get()}")
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${libs.versions.kotlin.get()}")
        resolutionStrategy.force("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.kotlinxCoroutinesCore.get()}")
        resolutionStrategy.force("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.kotlinxCoroutinesCore.get()}")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-Xskip-metadata-version-check")
            jvmTarget = "11"
        }
    }
}

// Apply a Kotlin compiler arg to skip metadata version checks for the root project as well.
// This allows consuming some libraries compiled with newer Kotlin metadata without immediately upgrading
// the project's Kotlin version. Use cautiously and consider upgrading Kotlin as a long-term fix.

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf("-Xskip-metadata-version-check")
        jvmTarget = "11"
    }
}
