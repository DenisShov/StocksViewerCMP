// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.deteKt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// Apply detekt to all subprojects that contain Kotlin code
subprojects {
    pluginManager.apply("io.gitlab.arturbosch.detekt")

    plugins.withId("io.gitlab.arturbosch.detekt") {
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            config.setFrom(files("$rootDir/tools/detekt/config.yml"))
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

        dependencies {
            val catalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
            "detektPlugins"(catalog.findLibrary("detekt-formatting").get())
            "detektPlugins"(catalog.findLibrary("detekt-libraries").get())
        }
    }
}