plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rndeveloper.myapplication.weather"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":domain:common"))
    implementation(project(":domain:weather"))
    implementation(project(":data:weather"))

//    Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

//    Retrofit
    implementation (libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)
}