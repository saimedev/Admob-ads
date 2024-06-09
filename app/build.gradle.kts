plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.saimedevs.compose.myads"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.saimedevs.compose.myads"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {

            resValue ("string", "admob_inter_ids", "ca-app-pub-3940256099942544/1033173712")
            resValue ("string", "admob_rewarded_ids", "ca-app-pub-3940256099942544/5224354917")
            resValue ("string", "admob_rewarded_inter_ids", "ca-app-pub-3940256099942544/5354046379")
            resValue ("string", "admob_native_ids", "ca-app-pub-3940256099942544/2247696110")
            resValue ("string", "admob_banner_ids", "ca-app-pub-3940256099942544/2014213617")
            resValue ("string", "admob_app_open_ids", "ca-app-pub-3940256099942544/9257395921")
            resValue ("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            resValue ("string", "admob_inter_ids", "ca-app-pub-3940256099942544/1033173712")
            resValue ("string", "admob_rewarded_ids", "ca-app-pub-3940256099942544/5224354917")
            resValue ("string", "admob_rewarded_inter_ids", "ca-app-pub-3940256099942544/5354046379")
            resValue ("string", "admob_native_ids", "ca-app-pub-3940256099942544/2247696110")
            resValue ("string", "admob_banner_ids", "ca-app-pub-3940256099942544/2014213617")
            resValue ("string", "admob_app_open_ids", "ca-app-pub-3940256099942544/9257395921")
            resValue ("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Admob ads
    implementation ("com.google.android.gms:play-services-ads:23.1.0")

    // SDP layout size && SSP Text sizes
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.1.0")



    // navigation components
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

   /* // View Model
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'*/

}