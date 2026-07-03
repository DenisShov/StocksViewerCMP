package com.test.app.stocksviewercmp

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Opt-in Compose Compiler reports and metrics.
 *
 * Enable with:
 *   ./gradlew assembleRelease -PenableComposeCompilerMetrics=true
 *
 * Output lands under `<module>/build/compose_compiler/`:
 *   - `<module>-classes.txt`      — every class the compiler saw + stability verdict
 *   - `<module>-composables.txt`  — every @Composable + restartable / skippable flags
 *   - `<module>-module.json`      — aggregate counters
 *
 * Reads the file at `<repo-root>/stability_config.conf` when present so the
 * compiler's classifications match any project stability contract.
 */
internal fun Project.configureComposeCompilerReports() {
    val enabled = providers.gradleProperty("enableComposeCompilerMetrics")
        .orNull
        ?.toBoolean() == true

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        if (enabled) {
            val outputDir = layout.buildDirectory.dir("compose_compiler")
            metricsDestination.set(outputDir)
            reportsDestination.set(outputDir)
        }

        val stabilityConfig = rootProject.layout.projectDirectory.file("stability_config.conf")
        if (stabilityConfig.asFile.exists()) {
            stabilityConfigurationFiles.add(stabilityConfig)
        }
    }
}
