plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.roommvvmdemo"
    compileSdk = 36  // ✅ Fixed: simple integer assignment, not a block

    defaultConfig {
        applicationId = "com.example.roommvvmdemo"
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
}

dependencies {
    val room_version = "2.6.1"        // ✅ Fixed: `val` instead of `def`
    val lifecycle_version = "2.8.7"   // ✅ Fixed: `val` instead of `def`

    implementation("androidx.room:room-runtime:$room_version")           // ✅ Fixed: double quotes → parentheses + string
    annotationProcessor("androidx.room:room-compiler:$room_version")     // ✅ Fixed
    implementation("androidx.room:room-ktx:$room_version")               // ✅ Fixed: room-livedata → room-ktx (includes LiveData support)

    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")  // ✅ Fixed
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")   // ✅ Fixed

    implementation("androidx.recyclerview:recyclerview:1.4.0")           // ✅ Fixed
    implementation("androidx.cardview:cardview:1.0.0")                   // ✅ Fixed
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}