plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    android {
        namespace = "com.core.resources"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))
            implementation(libs.koin.core)
        }
    }
}
