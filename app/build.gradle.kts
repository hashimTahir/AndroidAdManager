plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.hashim.admanager"
        minSdk = 21
        targetSdk = 32
        versionName = "1.0"
        versionCode = 1
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

            resValue("string", "AdMob_AppId", "ca-app-pub-3940256099942544~3347511713")
            resValue ("string", "Admob_BannerId", "/6499/example/banner")
            resValue ("string", "Admob_NativeAdvancedId", "/6499/example/native")
            resValue( "string", "Admob_InterstitialId", "/6499/example/interstitial")


            resValue( "string", "Mopub_BannerId", "b195f8dd8ded45fe847ad89ed1d016da")
            resValue ("string", "Mopub_InterstitialId", "24534e1901884e398f1253216226017e")
            resValue( "string", "Mopup_Native_BannerId", "11a17b188668469fb0412708c3d16813")


            resValue ("string", "Fb_InterstitialId", "262820941929301_262821001929295")
            resValue ("string", "Fb_NativeAdvancedId", "262820941929301_262820981929297")
            resValue ("string", "Fb_Native_BannerId", "262820941929301_262821005262628")
            resValue ("string", "Fb_BannerId", "262820941929301_262820988595963")
        }
    }
    viewBinding.isEnabled = true

}

dependencies {
    implementation(project(path = ":HAdManager"))
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
}