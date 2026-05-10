plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
}

kotlin {
    android {
        namespace = "com.core.designsystem"
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.material3)
            api(compose.foundation)
            api(compose.runtime)
            api(compose.ui)
            @Suppress("DEPRECATION")
            api(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(project(":core:commonresources"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
    }
}
