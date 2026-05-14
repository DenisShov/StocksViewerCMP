plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
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
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.runtime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
