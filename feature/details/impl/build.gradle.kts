plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "com.feature.details.impl"
        androidResources {
            enable = true
        }
        withHostTest {}
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:network"))
            implementation(project(":core:ui"))
            implementation(project(":core:navigation"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:resources"))
            implementation(project(":feature:details:api"))
            implementation(project(":core:component:favorites"))

            implementation(libs.material.icons.extended)

            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.lifecycle.viewmodel.compose.multiplatform)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.arrow.core)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor3)
            implementation(libs.vico.multiplatform)
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
