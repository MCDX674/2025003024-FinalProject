package com.example.weighttrack

import android.app.Application
import com.example.weighttrack.data.database.AppDatabase
import com.example.weighttrack.data.repository.WeightRepository
import com.example.weighttrack.datastore.UserPreferencesRepository

class WeightTrackApp : Application() {
    lateinit var userPrefsRepository: UserPreferencesRepository
    lateinit var weightRepository: WeightRepository

    override fun onCreate() {
        super.onCreate()
        userPrefsRepository = UserPreferencesRepository(this)
        val db = AppDatabase.getInstance(this)
        weightRepository = WeightRepository(db.weightRecordDao(), db.userProfileDao())
    }
}