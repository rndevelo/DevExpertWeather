plugins {
    id("rndeveloper.jvm.library")
}
dependencies {
    implementation(project(":domain:weather"))
    implementation(project(":data:weather"))
    implementation(project(":domain:location"))
    implementation(project(":data:location"))
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
}
