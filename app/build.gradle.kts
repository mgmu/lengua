plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
    id("org.jetbrains.dokka") version "1.9.20"
}

android {
    namespace = "dev.lengua"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.lengua"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    val lifecycleVersion = "2.8.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    val jupiterVersion = "5.10.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    // for instrumented tests on device or emulator, see https://github.com/mannodermaus/android-junit5
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")

    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.20")

    // Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation(libs.core.ktx)

    // Compose preview
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")
    debugImplementation(libs.androidx.ui.tooling)

    // Material 3
    implementation(libs.androidx.material3.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
