package io.qameta.allure.android.listeners

import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry
import io.qameta.allure.kotlin.listener.TestLifecycleListener
import io.qameta.allure.kotlin.model.TestResult

class InstrumentationTestLifecycleListener : TestLifecycleListener {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    
    override fun beforeTestWrite(result: TestResult) {
        val bundle = Bundle().apply {
            putString("allure-uuid", result.uuid)
            putString("allure-testcaseid", result.testCaseId)
            putString("allure-historyid", result.historyId)
        }
        
        instrumentation.sendStatus(CODE_IN_PROGRESS, bundle)
    }
    
    companion object {
        const val CODE_IN_PROGRESS = 2
    }
}
