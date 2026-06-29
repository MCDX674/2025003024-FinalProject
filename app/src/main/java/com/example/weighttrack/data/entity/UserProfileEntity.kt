package com.example.weighttrack.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val height: String,
    val targetWeight: String,
    val gender: String
)