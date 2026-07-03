package com.test.app.stocksviewercmp.shared.di

import com.core.common.constants.CommonConstants
import org.koin.core.context.startKoin
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
fun initKoin() {
    startKoin {
        properties(mapOf(CommonConstants.PROPERTY_IS_DEBUG_BUILD to Platform.isDebugBinary))
        modules(sharedModules)
    }
}
