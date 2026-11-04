package com.example.dscanner.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CompetitorWithProducts(
    @Embedded
    val competitor: CompetitorsEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "competitorId"
    )
    val products: List<ProductEditEntity>
)