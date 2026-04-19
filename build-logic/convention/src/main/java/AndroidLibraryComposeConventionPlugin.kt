import com.template.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("template.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureAndroidCompose()
        }
    }
}

