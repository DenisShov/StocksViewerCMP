plugins {
    `kotlin-dsl`
}

group = "com.test.app.stocksviewercmp.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "stocksviewercmp.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "stocksviewercmp.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidApplicationJacoco") {
            id = "stocksviewercmp.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }

        register("androidFeature") {
            id = "stocksviewercmp.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidLibrary") {
            id = "stocksviewercmp.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "stocksviewercmp.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidLibraryJacoco") {
            id = "stocksviewercmp.android.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }

        register("jvmLibrary") {
            id = "stocksviewercmp.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }

        register("androidKoin") {
            id = "stocksviewercmp.android.koin"
            implementationClass = "AndroidKoinConventionPlugin"
        }

        register("detekt") {
            id = "stocksviewercmp.detekt"
            implementationClass = "DetektConventionPlugin"
        }

    }
}
