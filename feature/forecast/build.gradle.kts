plugins {
    id("rndeveloper.android.feature")
    id("rndeveloper.di.library.compose")
}

android {
    namespace = "com.rndeveloper.myapplication.feature.forecast"
}

dependencies {
    implementation(project(":domain:weather"))
}