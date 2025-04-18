plugins {
    id("rndeveloper.jvm.library")
}
dependencies {
    implementation(project(":domain:common"))
    implementation(project(":domain:location"))
}