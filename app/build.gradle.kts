plugins {
    alias(libs.plugins.stocksviewercmp.android.application)
    alias(libs.plugins.stocksviewercmp.android.application.compose)
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

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.timber)
}
