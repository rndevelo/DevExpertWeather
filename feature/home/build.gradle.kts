plugins {
    id("rndeveloper.android.feature")
    id("rndeveloper.di.library.compose")
}

android {
    namespace = "com.rndeveloper.myapplication.feature.home"
}

dependencies {
    implementation(project(":domain:location"))
    implementation(project(":domain:weather"))
}