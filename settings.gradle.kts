pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
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

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

rootProject.name = "TemplateAndroidKotlin"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

// Core modules
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:presentation:designsystem")
include(":core:presentation:ui")

// Auth feature
include(":auth:domain")
include(":auth:data")
include(":auth:presentation")

// Home feature
include(":home:domain")
include(":home:data")
include(":home:presentation")

// Profile feature
include(":profile:domain")
include(":profile:data")
include(":profile:presentation")