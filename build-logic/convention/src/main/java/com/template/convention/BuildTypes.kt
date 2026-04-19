package com.template.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    extensionType: ExtensionType
) {
    when (extensionType) {
        ExtensionType.APPLICATION -> {
            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    buildConfig = true
                }
                buildTypes {
                    debug {
                        configureDebugBuildType()
                    }
                    release {
                        configureReleaseBuildType()
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }
        }
        ExtensionType.LIBRARY -> {
            extensions.configure<LibraryExtension> {
                buildFeatures {
                    buildConfig = true
                }
                buildTypes {
                    debug {
                        configureDebugBuildType()
                    }
                    release {
                        configureReleaseBuildType()
                        isMinifyEnabled = false
                        consumerProguardFiles("consumer-rules.pro")
                    }
                }
            }
        }
        ExtensionType.DYNAMIC_FEATURE -> {
            extensions.configure<DynamicFeatureExtension> {
                buildFeatures {
                    buildConfig = true
                }
                buildTypes {
                    debug {
                        configureDebugBuildType()
                    }
                    release {
                        configureReleaseBuildType()
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType() {
    buildConfigField("String", "BASE_URL", "\"https://dummyjson.com\"")
}

private fun BuildType.configureReleaseBuildType() {
    buildConfigField("String", "BASE_URL", "\"https://dummyjson.com\"")
}

