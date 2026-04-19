plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.template.android.room)
}

android {
    namespace = "com.template.core.database"
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.bundles.koin)
}
