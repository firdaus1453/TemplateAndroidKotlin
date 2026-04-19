plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.template.jvm.ktor)
}

android {
    namespace = "com.template.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)

    implementation(libs.bundles.koin)
    implementation(libs.timber)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.kotlinx.serialization.json)
}
