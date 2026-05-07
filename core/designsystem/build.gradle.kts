plugins {
    alias(libs.plugins.stocksviewercmp.android.library)
    alias(libs.plugins.stocksviewercmp.android.library.compose)
    alias(libs.plugins.stocksviewercmp.android.library.jacoco)
}

android {
    namespace = "com.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.util)
}