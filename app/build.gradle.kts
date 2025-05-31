import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  id("com.google.devtools.ksp") version "2.1.0-1.0.29"
  id("com.ncorti.ktfmt.gradle") version "0.22.0"
}

android {
  namespace = "de.pawcode.cardstore"
  compileSdk = 35

  defaultConfig {
    applicationId = "de.pawcode.cardstore"
    minSdk = 31
    targetSdk = 35

    val properties = Properties()
    val propertiesFile = rootProject.file("version.properties")
    if (propertiesFile.exists()) {
      properties.load(propertiesFile.inputStream())
    }

    this.versionCode = properties.getProperty("VERSION_CODE")?.toInt() ?: 1
    this.versionName = properties.getProperty("VERSION_NAME") ?: "1.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      storeFile = file("../keystore.jks")
      storePassword = System.getenv("KEYSTORE_PASSWORD")
      keyAlias = System.getenv("KEY_ALIAS")
      keyPassword = System.getenv("KEY_PASSWORD")
    }
  }

  buildTypes {
    release {
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
      ndk { debugSymbolLevel = "FULL" }
    }
    debug {
      applicationIdSuffix = ".debug"
      isDebuggable = true
      isMinifyEnabled = false
      isShrinkResources = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
  }

  kotlinOptions { jvmTarget = "19" }

  buildFeatures { compose = true }

  androidResources { generateLocaleConfig = true }
}

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.material.icons.extended)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.common)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.barcode.scanning)
  implementation(libs.colorpicker.compose)
  implementation(libs.composed.barcodes)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.play.services.code.scanner)
  implementation(libs.revealswipe)
  implementation(libs.review)
  implementation(libs.review.ktx)
  implementation(platform(libs.androidx.compose.bom))
  ksp(libs.androidx.room.compiler)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.ui.test.junit4)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  debugImplementation(libs.androidx.ui.test.manifest)
  debugImplementation(libs.androidx.ui.tooling)
}

ktfmt { googleStyle() }
