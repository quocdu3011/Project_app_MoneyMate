plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.moneymate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.moneymate"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.fragment)
    implementation(libs.activity)

    // Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.rxjava3)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Hilt
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)
    implementation(libs.hilt.work)
    annotationProcessor(libs.hilt.ext.compiler)

    // RxJava
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    // WorkManager
    implementation(libs.workmanager)

    // Biometric
    implementation(libs.biometric)

    // Lottie
    implementation(libs.lottie)

    // Splash Screen
    implementation(libs.splashscreen)

    // MPAndroidChart
    implementation(libs.mpandroidchart)

    // OpenCSV
    implementation(libs.opencsv)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Security
    implementation(libs.security.crypto)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
