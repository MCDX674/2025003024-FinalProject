package com.example.weighttrack.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_record")
data class WeightRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weight: Float,
    val date: String
)