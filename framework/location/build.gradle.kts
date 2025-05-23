plugins {
    id("rndeveloper.android.library")
    id("rndeveloper.android.room")
    id("rndeveloper.jvm.retrofit")
    id("rndeveloper.di.library")
}

android {
    namespace = "com.rndeveloper.myapplication.framework.location"
}

dependencies {
    implementation(project(":domain:location"))
    implementation(project(":data:location"))
    implementation(libs.play.services.location)
}