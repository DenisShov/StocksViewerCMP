package com.core.testing.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Sets up the Main dispatcher for testing.
 * Call in @BeforeTest.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun setupTestDispatcher(dispatcher: TestDispatcher = UnconfinedTestDispatcher()): TestDispatcher {
    Dispatchers.setMain(dispatcher)
    return dispatcher
}

/**
 * Resets the Main dispatcher after testing.
 * Call in @AfterTest.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun resetTestDispatcher() {
    Dispatchers.resetMain()
}
