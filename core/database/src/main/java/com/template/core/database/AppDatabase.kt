package com.template.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.template.core.database.dao.ProductDao
import com.template.core.database.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}
