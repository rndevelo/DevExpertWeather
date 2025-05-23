plugins {
    id("rndeveloper.jvm.library")
    id("rndeveloper.di.library")
}

dependencies{
    testImplementation(project(":test:unit"))
}