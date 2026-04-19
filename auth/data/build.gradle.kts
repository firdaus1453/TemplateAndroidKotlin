plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.template.jvm.ktor)
}

android {
    namespace = "com.template.auth.data"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(libs.bundles.koin)
}
