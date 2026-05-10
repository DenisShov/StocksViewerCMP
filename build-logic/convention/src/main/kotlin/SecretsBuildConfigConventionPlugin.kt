import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.util.Properties

/**
 * Convention plugin that generates a BuildConfig.kt file in commonMain
 * with secrets loaded from secrets.defaults.properties.
 *
 * Usage: Apply this plugin to any KMP module that needs access to secrets.
 * The generated BuildConfig will be in the module's package (derived from namespace).
 */
class SecretsBuildConfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val secretsFile = rootProject.file("secrets.defaults.properties")
            val secretsProperties = Properties().apply {
                if (secretsFile.exists()) {
                    secretsFile.inputStream().use { load(it) }
                }
            }
            val apiKey = secretsProperties.getProperty("API_KEY", "").trim('"')

            val generatedDir = layout.buildDirectory.dir("generated/source/buildConfig/commonMain")

            val generateBuildConfig = tasks.register("generateBuildConfig") {
                val outputDir = generatedDir
                val key = apiKey
                val pkg = "com.core.network"
                outputs.dir(outputDir)
                inputs.property("apiKey", key)
                doLast {
                    val dir = outputDir.get().asFile.resolve(pkg.replace('.', '/'))
                    dir.mkdirs()
                    dir.resolve("BuildConfig.kt").writeText(
                        """
                        |package $pkg
                        |
                        |object BuildConfig {
                        |    const val API_KEY: String = "$key"
                        |}
                        """.trimMargin()
                    )
                }
            }

            extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
                sourceSets.getByName("commonMain") {
                    kotlin.srcDir(generatedDir)
                }
            }

            tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
                dependsOn(generateBuildConfig)
            }
        }
    }
}
