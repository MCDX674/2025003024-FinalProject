package com.example.weighttrack.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weighttrack.data.dao.UserProfileDao
import com.example.weighttrack.data.dao.WeightRecordDao
import com.example.weighttrack.data.entity.UserProfileEntity
import com.example.weighttrack.data.entity.WeightRecordEntity

@Database(
    entities = [WeightRecordEntity::class, UserProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightRecordDao(): WeightRecordDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "weight_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}