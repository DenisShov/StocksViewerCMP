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

            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
    }
}
