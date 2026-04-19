import com.android.build.api.dsl.LibraryExtension
import com.template.convention.ExtensionType
import com.template.convention.configureBuildTypes
import com.template.convention.configureKotlinAndroid
import com.template.convention.libs
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlinx.kover")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                @Suppress("UnstableApiUsage")
                testOptions {
                    unitTests.all {
                        it.useJUnitPlatform()
                    }
                }
            }

            configureKotlinAndroid()

            configureBuildTypes(
                extensionType = ExtensionType.LIBRARY
            )

            // Kover: explicitly wire the debug variant so Kover instruments
            // testDebugUnitTest and finds compiled classes under AGP 9.
            extensions.configure<KoverProjectExtension> {
                currentProject {
                    createVariant("custom") {
                        add("debug", optional = true)
                    }
                }
            }

            // Disable release unit tests to avoid R8 issues during Kover
            tasks.matching { it.name == "testReleaseUnitTest" }.configureEach {
                enabled = false
            }

            dependencies {
                "testImplementation"(libs.findBundle("testing").get())
                "testRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
            }
        }
    }
}
