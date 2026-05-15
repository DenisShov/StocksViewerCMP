plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
}

kotlin {
    android {
        namespace = "com.core.designsystem"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.material3)
            api(libs.foundation)
            api(libs.runtime)
            api(libs.ui)
            api(libs.material.icons.extended)
            implementation(project(":core:resources"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
    }
}
