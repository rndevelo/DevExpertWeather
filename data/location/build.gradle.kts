plugins {
    id("rndeveloper.jvm.library")
    id("rndeveloper.di.library")
}

dependencies {
    implementation(project(":domain:location"))
    testImplementation(project(":test:unit"))
}