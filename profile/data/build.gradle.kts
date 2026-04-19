plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.template.jvm.ktor)
}

android {
    namespace = "com.template.profile.data"
}

dependencies {
    implementation(projects.profile.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(libs.bundles.koin)
}
