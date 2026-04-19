plugins {
    alias(libs.plugins.template.android.feature.ui)
}

android {
    namespace = "com.template.profile.presentation"
}

dependencies {
    implementation(projects.profile.domain)
    implementation(projects.core.domain)

    implementation(libs.coil.compose)
}
