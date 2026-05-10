package com.core.database.platform

import androidx.room.RoomDatabase
import com.core.database.StocksDatabase

expect fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<StocksDatabase>
