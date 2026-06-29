package com.example.weighttrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weighttrack.data.entity.WeightRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightRecordDao {
    // 新增体重记录
    @Insert
    suspend fun insertRecord(record: WeightRecordEntity)

    // 删除单条体重记录（匹配Repository里的调用）
    @Delete
    suspend fun deleteRecord(record: WeightRecordEntity)

    // 所有记录按主键id降序，最新添加的排在最顶部
    @Query("SELECT * FROM weight_record ORDER BY id DESC")
    fun getAllRecordsFlow(): Flow<List<WeightRecordEntity>>

    // 取倒序后的第一条，确保统计页拿到最新体重
    @Query("SELECT * FROM weight_record ORDER BY id DESC LIMIT 1")
    fun getLatestRecordFlow(): Flow<WeightRecordEntity?>

    // 记录总条数
    @Query("SELECT COUNT(*) FROM weight_record")
    fun getRecordCountFlow(): Flow<Int>
}