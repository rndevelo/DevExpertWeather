plugins {
    id("rndeveloper.android.library")
    id("rndeveloper.android.room")
    id("rndeveloper.jvm.retrofit")
    id("rndeveloper.di.library")
}

android {
    namespace = "com.rndeveloper.myapplication.framework.weather"
}

dependencies {
    implementation(project(":domain:weather"))
    implementation(project(":data:weather"))
    implementation(libs.gson)
}