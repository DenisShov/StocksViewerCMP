plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library)
}

kotlin {
    android {
        namespace = "com.core.testing"
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))
            api(project(":core:network"))
            api(libs.kotlinx.coroutines.test)
            api(libs.turbine)
        }
    }
}
