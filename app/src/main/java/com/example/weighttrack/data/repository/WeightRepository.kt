package com.example.weighttrack.data.repository

import kotlinx.coroutines.flow.Flow
import com.example.weighttrack.data.dao.UserProfileDao
import com.example.weighttrack.data.dao.WeightRecordDao
import com.example.weighttrack.data.entity.UserProfileEntity
import com.example.weighttrack.data.entity.WeightRecordEntity

class WeightRepository(
    private val weightRecordDao: WeightRecordDao,
    private val userProfileDao: UserProfileDao
) {
    // 体重记录
    fun getAllRecords(): Flow<List<WeightRecordEntity>> = weightRecordDao.getAllRecordsFlow()
    fun getLatestRecord(): Flow<WeightRecordEntity?> = weightRecordDao.getLatestRecordFlow()
    fun getRecordCount(): Flow<Int> = weightRecordDao.getRecordCountFlow()
    suspend fun insertRecord(record: WeightRecordEntity) = weightRecordDao.insertRecord(record)
    suspend fun deleteRecord(record: WeightRecordEntity) = weightRecordDao.deleteRecord(record)

    // 用户档案
    fun getUserProfile(): Flow<UserProfileEntity?> = userProfileDao.getProfileFlow()
    suspend fun saveProfile(profile: UserProfileEntity) = userProfileDao.saveProfile(profile)
}