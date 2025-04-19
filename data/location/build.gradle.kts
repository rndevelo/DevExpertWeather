plugins {
    id("rndeveloper.jvm.library")
    id("rndeveloper.di.library")
}
dependencies {
    implementation(project(":domain:common"))
    implementation(project(":domain:location"))
}