plugins {
    alias(libs.plugins.stocksviewercmp.android.feature)
    alias(libs.plugins.stocksviewercmp.android.library.compose)
    alias(libs.plugins.stocksviewercmp.android.koin)
    alias(libs.plugins.stocksviewercmp.android.library.jacoco)
}

android {
    namespace = "com.feature.favorites.impl"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:database"))
    implementation(project(":feature:favorites:api"))
    implementation(project(":feature:details:api"))
    implementation(project(":shared-library:favorites"))

    implementation(libs.androidx.compose.material3)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
