plugins {
    id("rndeveloper.android.feature")
}

android {
    namespace = "com.rndeveloper.myapplication.feature.forecast"
}

dependencies {
    implementation(project(":domain:weather"))
}