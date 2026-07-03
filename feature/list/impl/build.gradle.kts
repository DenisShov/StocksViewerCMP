plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "com.feature.list.impl"
        withHostTest {}
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:network"))
            implementation(project(":core:navigation"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:ui"))
            implementation(project(":core:resources"))
            implementation(project(":feature:list:api"))
            implementation(project(":feature:details:api"))

            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation3)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.arrow.core)
            implementation(libs.lifecycle.viewmodel.compose.multiplatform)
            implementation(libs.lifecycle.runtime.compose.multiplatform)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor3)
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }
    }
}
