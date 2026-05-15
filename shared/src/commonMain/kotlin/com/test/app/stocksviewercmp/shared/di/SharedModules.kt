package com.test.app.stocksviewercmp.shared.di

import com.core.database.di.databaseModule
import com.core.network.di.networkModule
import com.feature.details.impl.di.stockDetailsModule
import com.feature.favorites.impl.di.favoritesListModule
import com.feature.list.impl.di.stocksListModule
import com.sharedlibrary.favorites.di.favoritesModule
import core.commonresources.di.commonResourcesModule
import org.koin.core.module.Module

/**
 * Single source of truth for all Koin modules shared across platforms.
 * Both Android and iOS reference this list.
 */
val sharedModules: List<Module> = listOf(
    networkModule,
    databaseModule,
    commonResourcesModule,
    favoritesModule,
    stocksListModule,
    stockDetailsModule,
    favoritesListModule,
)
