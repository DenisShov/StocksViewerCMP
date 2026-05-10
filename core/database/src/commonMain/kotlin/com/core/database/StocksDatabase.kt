package com.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.core.database.dao.FavoriteStockDao
import com.core.database.entity.FavoriteStockEntity

@Database(
    entities = [FavoriteStockEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(StocksDatabaseConstructor::class)
abstract class StocksDatabase : RoomDatabase() {
    abstract fun favoriteStockDao(): FavoriteStockDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object StocksDatabaseConstructor : RoomDatabaseConstructor<StocksDatabase> {
    override fun initialize(): StocksDatabase
}
