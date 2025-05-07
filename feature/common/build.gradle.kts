plugins {
    id("rndeveloper.android.library.compose")
    id("rndeveloper.di.library.compose")
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.rndeveloper.myapplication.feature.common"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
