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
    alias(libs.plugins.kover)
}

kover {
    reports {
        filters {
            excludes {
                // Exclude generated code and DI modules
                classes(
                    "*_Factory",
                    "*_HiltModules*",
                    "*BuildConfig*",
                    "*_Impl",
                    "*.di.*Module*",
                )
                // Exclude Compose-generated code
                annotatedBy("androidx.compose.runtime.Composable")
            }
        }
    }
}

dependencies {
    kover(project(":core:navigation"))
    kover(project(":feature:list:impl"))
    kover(project(":feature:details:impl"))
}
