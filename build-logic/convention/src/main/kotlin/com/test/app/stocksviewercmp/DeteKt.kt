package com.test.app.stocksviewercmp

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

internal fun Project.configureDetekt(
    commonExtension: DetektExtension,
) {
    commonExtension.apply {
        this.config.setFrom(files(file("$rootDir/tools/detekt/config.yml")))
        autoCorrect = true
        parallel = true
    }
}
