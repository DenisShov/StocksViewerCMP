plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library)
}

kotlin {
    android {
        namespace = "com.sharedlibrary.favorites"
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))
            implementation(project(":core:database"))
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
