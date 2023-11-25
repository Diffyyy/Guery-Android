plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mobdeve.s13.kok.james.gueryandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobdeve.s13.kok.james.gueryandroid"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = org.gradle.api.JavaVersion.VERSION_11
        targetCompatibility = org.gradle.api.JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.paging:paging-runtime:3.2.1");
//  implementation("com.firebaseui:firebase-ui-firestore:4.3.2")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("androidx.media3:media3-common:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.8")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation ("androidx.media3:media3-exoplayer-dash:1.2.0")
    implementation ("androidx.media3:media3-ui:1.2.0")
}