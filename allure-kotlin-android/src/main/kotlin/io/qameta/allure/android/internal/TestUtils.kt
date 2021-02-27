package io.qameta.allure.android.internal

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.permission.PermissionRequester
import androidx.test.uiautomator.UiDevice
import java.io.File

/**
 * Gives information about the environment in which the tests are running:
 *  - @return true for Android device (real device or emulator)
 *  - @return false for emulated environment of Robolectric
 *
 * This is the same logic as present in [androidx.test.ext.junit.runners.AndroidJUnit4] for resolving class runner.
 */
@SuppressLint("DefaultLocale")
internal fun isDeviceTest(): Boolean =
    System.getProperty("java.runtime.name")?.toLowerCase()?.contains("android") ?: false

internal fun requestExternalStoragePermissions() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) return

    with(PermissionRequester()) {
        addPermissions("android.permission.WRITE_EXTERNAL_STORAGE")
        addPermissions("android.permission.READ_EXTERNAL_STORAGE")
        requestPermissions()
    }
}

/**
 * Retrieves [UiDevice] if it's available, otherwise null is returned.
 * In Robolectric tests [UiDevice] is inaccessible and this property serves as a safe way of accessing it.
 */
internal val uiDevice: UiDevice?
    get() = runCatching { UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) }
        .onFailure { Log.e("UiDevice", "UiDevice unavailable") }
        .getOrNull()

internal fun createTemporaryFile(prefix: String = "temp", suffix: String? = null): File {
    val cacheDir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir
    return createTempFile(prefix, suffix, cacheDir)
}