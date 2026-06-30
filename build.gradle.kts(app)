plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.zaebalo.xr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zaebalo.xr"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // ЖЕСТКИЙ БИНАРНЫЙ ИНЖЕКТ НАСТРОЕК PICO 4 НАПРЯМУЮ В ПАМЯТЬ GRADLE В ОБХОД XML-ФАЙЛОВ
        manifestPlaceholders["pico_advance_app"] = "true"
        manifestPlaceholders["pico_vr_mode"] = "com.picovr.intent.category.VR"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    // ДОБАВИТЬ СТРОГО СЮДА: Отключаем панику компилятора Compose по поводу версий Kotlin
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true")
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Чистый Jetpack Compose без использования XML
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
}
