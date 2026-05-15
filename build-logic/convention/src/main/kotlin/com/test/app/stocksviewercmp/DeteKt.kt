package com.test.app.stocksviewercmp

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

internal fun Project.configureDetekt(
    commonExtension: DetektExtension,
) {
    commonExtension.apply {
        config.setFrom(files(file("$rootDir/tools/detekt/config.yml")))
        autoCorrect = true
        parallel = true
        source.setFrom(
            files(
                "src/main/kotlin",
                "src/main/java",
                "src/commonMain/kotlin",
                "src/androidMain/kotlin",
                "src/iosMain/kotlin",
            )
        )
    }
}
