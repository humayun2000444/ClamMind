plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.clammind"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.clammind"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.volley)
    implementation (libs.material.v190)

    implementation (libs.okhttp3.okhttp)
    implementation (libs.recyclerview)
    implementation (libs.cardview)
    implementation (libs.okhttp.v490)
//    implementation ("com.google.firebase:firebase-auth:21.0.5")

//    classpath ("com.google.gms:google-services:4.3.15")

}