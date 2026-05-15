package com.test.app.stocksviewercmp

import android.app.Application
import com.test.app.stocksviewercmp.shared.di.sharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class StockViewerCMPApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@StockViewerCMPApplication)
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            modules(sharedModules)
        }
    }
}
