package com.example.dscanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_edit",
    foreignKeys = [
        ForeignKey(
            entity = CompetitorsEntity::class,
            parentColumns = ["id"],
            childColumns = ["competitorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("competitorId")]
)

data class ProductEditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val barCode: String,
    val price: Double,
    val competitorId: Long
)