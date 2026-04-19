plugins {
    alias(libs.plugins.template.android.library.compose)
}

android {
    namespace = "com.template.core.presentation.ui"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.koin.compose)
}
