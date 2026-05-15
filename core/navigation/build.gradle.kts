plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "com.core.navigation"
        withHostTest {}
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))

            api(libs.navigation3.ui.multiplatform)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
