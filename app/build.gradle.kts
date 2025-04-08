plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.nsu_alarmy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nsu_alarmy"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        // 뷰바인딩 기능 추가
        viewBinding = true
        // 데이터바인딩 기능 추가
        dataBinding = true
    }
}

dependencies {
    // JetPack
    //  Activity 기반으로 빌드된 구성가능한 API에 접근
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("com.google.android.material:material:1.10.0")
    //  이전 API 버전의 플랫폼에서 새 API에 접근 가능
    implementation("androidx.appcompat:appcompat-resources:1.7.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    //  최신 플랫폼 기능과 API를 타겟팅하는 동시에 이전 기기도 지원
    implementation("androidx.core:core:1.13.1")
    //  수명 주기
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    //  도구 및 다른 개발자가 앱의 코드를 이해하는 데 도움이 되는 메타데이터를 노출
    implementation("androidx.annotation:annotation:1.9.1")
    //  컴포저블 함수를 이용해 UI 정의(모양, 데이터 의존)
    implementation("androidx.compose.ui:ui:1.7.8")
    //  ConstrainLayout
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-beta01")
    implementation("androidx.compose.material3:material3:1.3.1")
    //  Fragment
    implementation("androidx.fragment:fragment-ktx:1.8.6")

    //  날짜 버튼 관련
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.foundation.android)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /* Firebase */
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Firestore
    implementation("com.google.firebase:firebase-firestore")


}