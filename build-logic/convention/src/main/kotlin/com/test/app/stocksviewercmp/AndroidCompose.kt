package com.test.app.stocksviewercmp

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Configure Compose-specific options.
 */
internal fun Project.configureAndroidCompose(
    @Suppress("UNUSED_PARAMETER") extension: Any,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("implementation", libs.findLibrary("androidx-activity-compose").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())

        add("androidTestImplementation", platform(bom))
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
    }
}
