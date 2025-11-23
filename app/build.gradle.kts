import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.jetbrains.kotlin.serialization)
  id("com.google.devtools.ksp") version "2.3.3"
  id("com.ncorti.ktfmt.gradle") version "0.25.0"
}

android {
  namespace = "de.pawcode.cardstore"
  compileSdk = 36

  defaultConfig {
    applicationId = "de.pawcode.cardstore"
    minSdk = 31
    targetSdk = 36

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

  buildFeatures { compose = true }

  androidResources { generateLocaleConfig = true }
}

kotlin { compilerOptions { jvmTarget = JvmTarget.JVM_19 } }

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.biometric)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.navigation3.ui)
  implementation(libs.androidx.room.common)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.barcode.scanning)
  implementation(libs.colorpicker.compose)
  implementation(libs.composed.barcodes)
  implementation(libs.kotlinx.serialization.core)
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
