plugins {
    alias(libs.plugins.template.android.feature.ui)
}

android {
    namespace = "com.template.home.presentation"
}

dependencies {
    implementation(projects.home.domain)
    implementation(projects.core.domain)

    implementation(libs.coil.compose)
}
