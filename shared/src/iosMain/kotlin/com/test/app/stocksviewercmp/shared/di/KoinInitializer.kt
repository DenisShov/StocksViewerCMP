package com.test.app.stocksviewercmp.shared.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedModules)
    }
}
