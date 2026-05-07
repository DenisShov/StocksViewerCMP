plugins {
    alias(libs.plugins.stocksviewercmp.android.feature)
    alias(libs.plugins.stocksviewercmp.android.koin)
    alias(libs.plugins.stocksviewercmp.android.library.jacoco)
    alias(libs.plugins.stocksviewercmp.android.library.compose)
}

android {
    namespace = "com.feature.details.impl"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:details:api"))
    implementation(project(":shared-library:favorites"))

    implementation(libs.timber)
    implementation(libs.vico.compose.m3)
    implementation(libs.coil)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}