package com.example.dscanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dscanner.data.local.dao.CompetitorsDao
import com.example.dscanner.data.local.entity.CompetitorsEntity
import com.example.dscanner.data.local.entity.ProductEditEntity
import com.example.dscanner.data.local.entity.ProductEntity

@Database(
    entities = [CompetitorsEntity::class, ProductEntity::class, ProductEditEntity::class],
    version = 9
)
abstract class ProductDataBase : RoomDatabase() {
    abstract fun competitorsDao(): CompetitorsDao
}