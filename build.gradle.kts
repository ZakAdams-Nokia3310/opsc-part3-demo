buildscript {
    repositories {
        google() // Ensure Google repository is included
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0") // Use the correct Android Gradle Plugin version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0") // Adjust to match your Kotlin version
        classpath("com.google.gms:google-services:4.3.15") // Google Services plugin for Firebase
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.4") // Add the Crashlytics Gradle plugin
    }
}

allprojects {
    repositories {
        google() // Ensure Google repository is included
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
