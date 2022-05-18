plugins {
    id("com.android.library")
    id("kotlin-android")
    `maven-publish`
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32
        consumerProguardFiles("consumer-rules.pro")

    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        debug {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    viewBinding.isEnabled = true


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("com.facebook.shimmer:shimmer:0.5.0@aar")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.google.android.gms:play-services-ads:20.6.0")
    implementation("com.facebook.android:audience-network-sdk:6.10.0")
    implementation("com.google.ads.mediation:facebook:6.10.0.0")
    implementation("com.mopub.mediation:facebookaudiencenetwork:6.5.1.0")
    implementation("com.applovin:applovin-sdk:11.1.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    implementation("com.mopub:mopub-sdk:5.18.0@aar") {
        isTransitive = true

    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("debug") {
                from(components["debug"])
                artifactId = "HAdManager"
                groupId = "com.github.hashimTahir"
                version = "1.6"
            }
            create<MavenPublication>("release") {
                from(components["release"])
                artifactId = "HAdManager"
                groupId = "com.github.hashimTahir"
                version = "1.6"
            }
        }
    }
}