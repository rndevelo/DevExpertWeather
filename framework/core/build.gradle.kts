plugins {
    id("rndeveloper.android.library")
    id("rndeveloper.android.room")
    id("rndeveloper.jvm.retrofit")
    id("rndeveloper.di.library")
}

android {
    namespace = "com.rndeveloper.myapplication.framework.core"
}

dependencies {
    implementation(project(":domain:weather"))
    implementation(project(":framework:location"))
    implementation(project(":framework:weather"))
    implementation(libs.gson)
}
