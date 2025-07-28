import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 36
    namespace = "com.petersommerhoff.kudoofinal"
    defaultConfig {
        applicationId = "rj.todos"
        minSdk = 16
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        multiDexEnabled = true

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("config") {
            storeFile = project.rootProject.file("sign.jks")
            storePassword = "android"
            keyPassword = "android"
            keyAlias = "key"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
            signingConfig = signingConfigs.getByName("config")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget("17"))
        }
    }

}

configurations.all {
    resolutionStrategy {
        exclude(group = "com.google.android.material", module = "material")
    }
}

dependencies {
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.huanli233.materialcompat:material:1.12.0-alpha01")
    val roomVersion = "2.6.0"  // Adjust to newest version if you want to
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    val coroutinesVersion = "+"
    // Use newest version if you want (might differ)
    //noinspection GradleDynamicVersion
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    //noinspection GradleDynamicVersion
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
