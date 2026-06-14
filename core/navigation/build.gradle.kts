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
            api(libs.lifecycle.viewmodel.navigation3.multiplatform)

            implementation(libs.koin.core)
            implementation(libs.koin.compose.navigation3)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotest.property)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
