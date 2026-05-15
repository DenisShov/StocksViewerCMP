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
            implementation(project(":core:resources"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
    }
}
