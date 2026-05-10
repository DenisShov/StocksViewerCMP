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
            @Suppress("DEPRECATION")
            implementation(compose.foundation)
            @Suppress("DEPRECATION")
            implementation(compose.material3)
            @Suppress("DEPRECATION")
            implementation(compose.runtime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
