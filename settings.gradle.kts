pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // Ensure this is included for Kotlin and other Gradle plugins
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "SyncUp_Demo"
include(":app")
 