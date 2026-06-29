package com.example.weighttrack.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

// 全局内存数据，所有页面共享，重启App会清空
object AppData {
    // 所有体重记录
    val weightRecords = mutableStateListOf<WeightRecord>()

    // 个人档案设置
    val height = mutableStateOf("")
    val targetWeight = mutableStateOf("")
    val gender = mutableStateOf("")
}

// 体重记录数据类
data class WeightRecord(
    val weight: Float,
    val date: String
)