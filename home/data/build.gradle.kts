plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.template.jvm.ktor)
}

android {
    namespace = "com.template.home.data"
}

dependencies {
    implementation(projects.home.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)

    implementation(libs.bundles.koin)
    implementation(libs.timber)
}
