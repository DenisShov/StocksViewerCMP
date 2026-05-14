plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

kotlin {
    android {
        namespace = "com.core.commonresources"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            implementation(libs.components.resources)

            implementation(libs.runtime)
            implementation(libs.koin.core)
        }
    }
}
