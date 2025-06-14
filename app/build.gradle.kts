import java.util.Properties

plugins {
    id("rndeveloper.android.application")
    id("rndeveloper.android.application.compose")
    id("rndeveloper.di.library.compose")
}

android {
    namespace = "com.rndeveloper.myapplication"

    defaultConfig {
        applicationId = "com.rndeveloper.myapplication"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.rndeveloper.myapplication.di.HiltTestRunner"

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

    implementation(project(":domain:location"))
    implementation(project(":domain:weather"))
    implementation(project(":data:location"))
    implementation(project(":data:weather"))
    implementation(project(":framework:core"))
    implementation(project(":framework:location"))
    implementation(project(":framework:weather"))
    implementation(project(":feature:common"))
    implementation(project(":feature:home"))
    implementation(project(":feature:forecast"))

//    Navigation
    implementation(libs.androidx.navigation.compose)

//    Hilt Test
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

//    Room Test
    androidTestImplementation(libs.androidx.room.ktx)
    kspAndroidTest(libs.androidx.room.compiler)

//    Mock Web Server
    androidTestImplementation(libs.okhttp.mockwebserver)
}