import com.template.convention.configureKotlinJvm
import com.template.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class JvmLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("org.jetbrains.kotlin.jvm")
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            configureKotlinJvm()

            tasks.withType<Test> {
                useJUnitPlatform()
            }

            dependencies {
                "testImplementation"(libs.findBundle("testing").get())
                "testRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
            }
        }
    }
}
