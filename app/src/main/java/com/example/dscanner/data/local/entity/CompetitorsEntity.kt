package com.example.dscanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "competitors")
data class CompetitorsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)