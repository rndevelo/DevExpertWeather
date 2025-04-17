plugins {
    id("rndeveloper.android.library")
    id("rndeveloper.android.room")
    id("rndeveloper.jvm.retrofit")
}

android {
    namespace = "com.rndeveloper.myapplication.framework.weather"
}

dependencies {
    implementation(project(":domain:common"))
    implementation(project(":domain:weather"))
    implementation(project(":data:weather"))
    implementation(libs.gson)
}