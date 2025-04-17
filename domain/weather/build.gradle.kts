plugins {
    id("rndeveloper.jvm.library")
}

dependencies {
    implementation(project(":domain:common"))
    implementation(libs.kotlinx.coroutines.core)
}