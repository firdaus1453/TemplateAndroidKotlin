import com.android.build.api.dsl.DynamicFeatureExtension
import com.template.convention.ExtensionType
import com.template.convention.configureAndroidCompose
import com.template.convention.configureBuildTypes
import com.template.convention.configureKotlinAndroid
import com.template.convention.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidDynamicFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.dynamic-feature")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureKotlinAndroid()
            configureAndroidCompose()
            configureBuildTypes(
                extensionType = ExtensionType.DYNAMIC_FEATURE
            )

            dependencies {
                addUiLayerDependencies(target)
                "testImplementation"(kotlin("test"))
            }
        }
    }
}

