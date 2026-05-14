plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
}

kotlin {
    android {
        namespace = "com.feature.favorites.impl"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:navigation"))
            implementation(project(":core:database"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:commonresources"))
            implementation(project(":feature:favorites:api"))
            implementation(project(":feature:details:api"))
            implementation(project(":shared-library:favorites"))

            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.lifecycle.viewmodel.compose.multiplatform)
            implementation(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
