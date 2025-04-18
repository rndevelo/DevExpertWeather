plugins {
    id("rndeveloper.android.library")
}

android {
    namespace = "com.rndeveloper.myapplication.framework.location"
}

dependencies {
    implementation(project(":domain:common"))
    implementation(project(":data:location"))
    implementation(libs.play.services.location)
}