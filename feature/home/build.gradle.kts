plugins {
    id("rndeveloper.android.feature")
}

android {
    namespace = "com.rndeveloper.myapplication.feature.home"
}

dependencies {
    implementation(project(":domain:common"))
    implementation(project(":domain:location"))
    implementation(project(":domain:weather"))
}