package com.core.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.core.database.StocksDatabase
import com.core.database.dao.FavoriteStockDao
import com.core.database.platform.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.scope.Scope
import org.koin.dsl.module

val databaseModule = module {
    single<StocksDatabase> {
        getDatabaseBuilder(platformContext())
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    factory<FavoriteStockDao> { get<StocksDatabase>().favoriteStockDao() }
}

internal expect fun Scope.platformContext(): Any?
