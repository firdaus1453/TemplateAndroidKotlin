plugins {
    alias(libs.plugins.template.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
}
