package com.example.dscanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "competitor_product")
data class CompetitorProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val competitorId: Long,
    val productId: Long
)