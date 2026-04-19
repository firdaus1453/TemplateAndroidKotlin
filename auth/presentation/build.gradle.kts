plugins {
    alias(libs.plugins.template.android.feature.ui)
}

android {
    namespace = "com.template.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}
