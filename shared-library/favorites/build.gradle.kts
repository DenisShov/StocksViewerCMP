plugins {
    alias(libs.plugins.stocksviewercmp.android.library)
    alias(libs.plugins.stocksviewercmp.android.koin)
}

android {
    namespace = "com.sharedlibrary.favorites"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":core:common"))
    implementation(project(":core:database"))

    testImplementation(project(":core:testing"))
}