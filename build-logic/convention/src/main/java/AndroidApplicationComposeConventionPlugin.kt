import com.template.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("template.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            configureAndroidCompose()
        }
    }
}

