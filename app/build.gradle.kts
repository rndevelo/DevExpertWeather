import java.util.Properties

plugins {
    id("rndeveloper.android.application")
    id("rndeveloper.android.application.compose")
    id("rndeveloper.di.library.compose")
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.rndeveloper.myapplication"

    defaultConfig {
        applicationId = "com.rndeveloper.myapplication"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").readText().byteInputStream())

        val wsApiKey = properties.getProperty("WS_API_KEY", "")
        buildConfigField("String", "WS_API_KEY", "\"$wsApiKey\"")
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
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(":domain:common"))
    implementation(project(":domain:location"))
    implementation(project(":domain:weather"))
    implementation(project(":data:location"))
    implementation(project(":data:weather"))
    implementation(project(":framework:location"))
    implementation(project(":framework:weather"))
    implementation(project(":feature:common"))
    implementation(project(":feature:home"))
    implementation(project(":feature:forecast"))

//    Navigation
    implementation(libs.androidx.navigation.compose)

//    Serialization
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}