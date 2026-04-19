plugins {
    alias(libs.plugins.template.jvm.library)
}

dependencies {
    implementation(projects.core.domain)

    testImplementation(libs.kotlinx.coroutines.test)
}
