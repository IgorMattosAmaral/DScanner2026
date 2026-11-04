package com.example.dscanner.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CompetitorsProducts(
    @Embedded val competitors: CompetitorsEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "competitorsId"
    )
    val products: List<ProductEntity>
)