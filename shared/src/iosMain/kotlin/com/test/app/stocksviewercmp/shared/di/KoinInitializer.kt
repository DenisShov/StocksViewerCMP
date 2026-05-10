package com.test.app.stocksviewercmp.shared.di

import com.core.common.di.commonModule
import com.core.database.di.databaseModule
import com.core.network.di.networkModule
import com.feature.details.impl.di.stockDetailsModule
import com.feature.favorites.impl.di.favoritesListModule
import com.feature.list.impl.di.stocksListModule
import com.sharedlibrary.favorites.di.favoritesModule
import core.commonresources.di.commonResourcesModule
import org.koin.core.context.startKoin

val sharedModules = listOf(
    networkModule,
    databaseModule,
    commonResourcesModule,
    commonModule,
    favoritesModule,
    stocksListModule,
    stockDetailsModule,
    favoritesListModule,
)

fun initKoin() {
    startKoin {
        modules(sharedModules)
    }
}
