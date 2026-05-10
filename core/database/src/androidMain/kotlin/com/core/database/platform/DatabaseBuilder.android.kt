package com.core.database.platform

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.core.database.StocksDatabase

actual fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<StocksDatabase> {
    val appContext = context as Context
    val dbFile = appContext.getDatabasePath("stocks_database.db")
    return Room.databaseBuilder<StocksDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
