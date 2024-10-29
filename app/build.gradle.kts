plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "za.varsitycollege.syncup_demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "za.varsitycollege.syncup_demo"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Core Android libraries
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // CardView library
    implementation("androidx.cardview:cardview:1.0.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1") // for Glide annotation processing

    // Unit and UI testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Firebase libraries
    implementation(platform("com.google.firebase:firebase-bom:31.0.0")) // Use BOM to manage Firebase versions
    implementation("com.google.firebase:firebase-auth-ktx")             // Firebase Authentication
    implementation("com.google.firebase:firebase-database-ktx")         // Firebase Realtime Database

    // Optional: Firebase Storage if you need file uploads
    implementation("com.google.firebase:firebase-storage-ktx")

    // Optional: Firebase Analytics (if needed)
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Optional: Firebase Cloud Messaging (for push notifications)
    implementation("com.google.firebase:firebase-messaging-ktx")
}
