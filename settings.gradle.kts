pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Sinaliza"

// App module
include(":app")

// Core modules
include(":core")
include(":core-ui")

// Feature modules
include(":feature-home")
include(":feature-report")
include(":feature-map")
include(":feature-profile")
