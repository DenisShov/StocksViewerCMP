import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

plugins {
    alias(libs.plugins.stocksviewercmp.kmp.library.compose)
}

kotlin {
    android {
        namespace = "com.test.app.stocksviewercmp.shared"
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "app"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:navigation"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:common"))
            implementation(project(":core:resources"))
            implementation(project(":core:network"))
            implementation(project(":core:database"))
            implementation(project(":core:ui"))
            implementation(project(":core:component:favorites"))
            implementation(project(":feature:list:api"))
            implementation(project(":feature:list:impl"))
            implementation(project(":feature:details:api"))
            implementation(project(":feature:details:impl"))
            implementation(project(":feature:favorites:api"))
            implementation(project(":feature:favorites:impl"))

            implementation(libs.material.icons.extended)

            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}
