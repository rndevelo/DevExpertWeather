pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DevExpertWeather"

include(":app")

include(":domain:weather")
include(":domain:location")

include(":data:location")
include(":data:weather")

include(":framework:core")
include(":framework:location")
include(":framework:weather")

include(":feature:common")
include(":feature:home")
include(":feature:forecast")
