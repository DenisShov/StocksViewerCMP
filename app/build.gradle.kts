plugins {
    alias(libs.plugins.stocksviewercmp.android.application)
    alias(libs.plugins.stocksviewercmp.android.application.compose)
    alias(libs.plugins.stocksviewercmp.android.koin)
    alias(libs.plugins.stocksviewercmp.android.application.jacoco)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.test.app.stocksviewercmp"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.test.app.stocksviewercmp"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":shared-library:favorites"))
    implementation(project(":feature:list:api"))
    implementation(project(":feature:list:impl"))
    implementation(project(":feature:details:api"))
    implementation(project(":feature:details:impl"))
    implementation(project(":feature:favorites:api"))
    implementation(project(":feature:favorites:impl"))

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.timber)
}
