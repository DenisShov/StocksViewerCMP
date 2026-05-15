import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("stocksviewercmp.kmp.library")
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            pluginManager.apply("stocksviewercmp.detekt")

            val compose = extensions.getByType(ComposeExtension::class.java).dependencies

            extensions.getByType(KotlinMultiplatformExtension::class.java).apply {
                sourceSets.getByName("commonMain") {
                    dependencies {
                        @Suppress("DEPRECATION")
                        implementation(compose.runtime)
                        @Suppress("DEPRECATION")
                        implementation(compose.foundation)
                        @Suppress("DEPRECATION")
                        implementation(compose.material3)
                        @Suppress("DEPRECATION")
                        implementation(compose.ui)
                        @Suppress("DEPRECATION")
                        implementation(compose.components.resources)
                    }
                }
            }
        }
    }
}
