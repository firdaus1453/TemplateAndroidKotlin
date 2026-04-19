package com.template.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType

internal fun Project.configureAndroidCompose() {
    extensions.findByType<ApplicationExtension>()?.apply {
        buildFeatures { compose = true }
    }

    extensions.findByType<LibraryExtension>()?.apply {
        buildFeatures { compose = true }
    }

    extensions.findByType<DynamicFeatureExtension>()?.apply {
        buildFeatures { compose = true }
    }

    dependencies {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        "implementation"(platform(bom))
        "androidTestImplementation"(platform(bom))
        "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
    }
}

