plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication"){
            id = "rndeveloper.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "rndeveloper.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "rndeveloper.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidLibrary") {
            id = "rndeveloper.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "rndeveloper.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("jvmLibrary") {
            id = "rndeveloper.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "rndeveloper.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmRetrofit") {
            id = "rndeveloper.jvm.retrofit"
            implementationClass = "JvmRetrofitConventionPlugin"
        }
        register("diLibrary") {
            id = "rndeveloper.di.library"
            implementationClass = "DiLibraryConventionPlugin"
        }
        register("diLibraryCompose") {
            id = "rndeveloper.di.library.compose"
            implementationClass = "DiLibraryComposeConventionPlugin"
        }
    }
}