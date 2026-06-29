package com.example.weighttrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weighttrack.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SettingsViewModel(
    private val prefsRepo: UserPreferencesRepository
) : ViewModel() {
    // 原有持久化数据
    val height = prefsRepo.heightFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val targetWeight = prefsRepo.targetWeightFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val gender = prefsRepo.genderFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val darkMode = prefsRepo.darkModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // ========== 新增：出生日期状态流 ==========
    val birthDate = prefsRepo.birthDateFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    // ========== 修改：保存个人档案新增出生日期参数 ==========
    fun saveProfile(h: String, w: String, g: String, birthDate: String) {
        viewModelScope.launch {
            prefsRepo.saveUserProfile(h, w, g, birthDate)
        }
    }

    // 切换深色模式（原有不变）
    fun switchDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            prefsRepo.setDarkMode(isDark)
        }
    }

    // ========== 新增：根据出生日期自动计算周岁年龄 ==========
    fun calculateAge(birthDateStr: String): Int {
        if (birthDateStr.isBlank()) return 0
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val birth = format.parse(birthDateStr) ?: return 0
            val birthCal = Calendar.getInstance().apply { time = birth }
            val nowCal = Calendar.getInstance()

            var age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
            // 今年生日还没到，年龄减1
            if (nowCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            if (age < 0) 0 else age
        } catch (e: Exception) {
            0 // 格式错误返回0
        }
    }
}