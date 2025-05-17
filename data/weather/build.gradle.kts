plugins {
    id("rndeveloper.jvm.library")
    id("rndeveloper.di.library")
}

dependencies {
    implementation(project(":domain:weather"))
    implementation(libs.kotlinx.coroutines.core)
}