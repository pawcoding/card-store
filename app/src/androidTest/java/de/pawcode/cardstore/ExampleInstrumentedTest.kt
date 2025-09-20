package de.pawcode.cardstore

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    // Package name should be either release version or debug version
    val expectedPackages = listOf("de.pawcode.cardstore", "de.pawcode.cardstore.debug")
    assertTrue(
      "Package name should be one of: $expectedPackages",
      expectedPackages.contains(appContext.packageName),
    )
  }

  @Test
  fun debugAppHasCorrectName() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val appName = appContext.getString(appContext.applicationInfo.labelRes)

    if (appContext.packageName.endsWith(".debug")) {
      // Debug build should have "Debug" in the name
      assertTrue(
        "Debug app should contain 'Debug' in the name, but was: $appName",
        appName.contains("Debug"),
      )
    } else {
      // Release build should not have "Debug" in the name
      assertTrue(
        "Release app should not contain 'Debug' in the name, but was: $appName",
        !appName.contains("Debug"),
      )
    }
  }
}
