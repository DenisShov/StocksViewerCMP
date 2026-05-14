plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
}

kotlin {
    android {
        namespace = "com.core.ui"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:designsystem"))
            implementation(project(":core:commonresources"))

            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.runtime)
            implementation(compose.ui)
            implementation(libs.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
    }
}
