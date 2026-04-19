// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kover)
}

dependencies {
    // Kover: add all modules with tests for merged coverage report
    kover(project(":core:domain"))
    kover(project(":core:data"))
    kover(project(":core:database"))
    kover(project(":core:presentation:ui"))
    kover(project(":core:presentation:designsystem"))
    kover(project(":auth:domain"))
    kover(project(":auth:data"))
    kover(project(":auth:presentation"))
    kover(project(":home:domain"))
    kover(project(":home:data"))
    kover(project(":home:presentation"))
    kover(project(":profile:domain"))
    kover(project(":profile:data"))
    kover(project(":profile:presentation"))
}

kover {
    merge {
        allProjects {
            it.name != rootProject.name
        }
        createVariant("merged") {
            add("custom", optional = true)  // Android modules custom variant
            add("jvm", optional = true)     // JVM modules (domain)
        }
    }

    reports {
        filters {
            excludes {
                // ── Generated code ──
                classes(
                    "*_Factory",
                    "*_HiltModules*",
                    "*_Impl*",
                    "*.BuildConfig",
                    "*\$\$serializer",             // kotlinx.serialization generated
                    "*\$Companion",                // companion object boilerplate
                    "*\$DefaultImpls",             // Kotlin interface default-param bridge methods
                )

                // ── DI modules ──
                classes(
                    "*.di.*",
                )

                // ── Compose UI (not unit-testable without instrumentation) ──
                classes(
                    "*ComposableSingletons*",
                    "*_Preview*",
                    "*ScreenKt*",                  // Compose Screen file-level classes
                    "*Screen\$*",                   // lambdas inside Screen composables
                    "*DialogKt*",                   // Dialog composable file classes
                    "*ObserveAsEventsKt*",          // Compose lifecycle utility
                )

                // ── App-level wiring (not unit-testable) ──
                classes(
                    "*Activity*",
                    "*Application*",
                    "*.Routes*",
                    "*.MainScreen*",
                    "*.NavigationRoot*",
                    "*.MainViewModel*",
                    "*.MainState*",
                    "*.TemplateApp*",
                )

                // ── Design system (Compose theme, colors, typography — not unit-testable) ──
                packages(
                    "com.template.core.presentation.designsystem",
                )

                // ── Infrastructure requiring integration tests ──
                // Only exclude specific implementation classes
                classes(
                    "*HttpClientFactory*",
                    "*HttpClientExt*",
                    "*EncryptedSessionStorage*",
                    "*AppDatabase*",
                    "*AppDatabase_*",
                    "*.RoomLocal*",                // Room DAO wrappers
                    "*.KtorRemote*",               // Ktor network implementations
                    "*.KtorAuth*",
                    "*.KtorProfile*",
                    "*.SharedPrefsApp*",
                )

                // ── Data layer internals tied to Android framework ──
                classes(
                    "*AuthInfoSerializable*",       // Used by EncryptedSessionStorage only
                    "*AuthInfoMapper*",             // Maps to/from AuthInfoSerializable
                    "*AccessTokenDto*",             // Ktor response DTO
                )

                // ── Presentation utilities requiring Android Context ──
                classes(
                    "*UiText*",                     // Needs Context / @Composable
                    "*DataErrorToTextKt*",          // Uses R.string.*
                )

                // ── DTO classes (data holders with serialization boilerplate) ──
                classes(
                    "*Dto",
                    "*Dto\$*",
                    "*Request",
                    "*Request\$*",
                    "*Response",
                    "*Response\$*",
                )
            }
        }

        verify {
            rule("Minimum coverage") {
                minBound(90)
            }
        }
    }
}