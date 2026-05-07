plugins {
    alias(libs.plugins.stocksviewercmp.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.feature.list.api"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:navigation"))

    testImplementation(project(":core:testing"))
}