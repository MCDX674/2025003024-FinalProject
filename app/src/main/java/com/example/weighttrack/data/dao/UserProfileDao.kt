package com.example.weighttrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.weighttrack.data.entity.UserProfileEntity

@Dao
interface UserProfileDao {

    // 插入/更新个人档案（主键固定1，全局仅一条）
    @Insert
    suspend fun insertProfile(profile: UserProfileEntity)

    @Insert
    suspend fun saveProfile(profile: UserProfileEntity)

    @Update
    suspend fun updateProfile(profile: UserProfileEntity)

    // 读取个人档案
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getProfileFlow(): Flow<UserProfileEntity?>
}