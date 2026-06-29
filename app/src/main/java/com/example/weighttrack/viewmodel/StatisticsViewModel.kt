package com.example.weighttrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weighttrack.data.entity.WeightRecordEntity
import com.example.weighttrack.data.repository.WeightRepository
import com.example.weighttrack.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatisticsViewModel(
    private val weightRepo: WeightRepository,
    private val prefsRepo: UserPreferencesRepository
) : ViewModel() {
    // 体重记录数据流
    val allRecords = weightRepo.getAllRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val latestRecord = weightRepo.getLatestRecord()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val recordCount = weightRepo.getRecordCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // 用户档案基础数据
    val height = prefsRepo.heightFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val targetWeight = prefsRepo.targetWeightFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val gender = prefsRepo.genderFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val birthDate = prefsRepo.birthDateFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    val darkMode = prefsRepo.darkModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // 励志语录库 Pair(内容,作者)
    private val quoteList = listOf(
        Pair("自律即自由，坚持运动遇见更好的自己", "佚名"),
        Pair("不要让懒惰，消耗你的健康与热爱", "健康格言"),
        Pair("微小的坚持，终将换来体态与身心的蜕变", "健身语录"),
        Pair("好好吃饭，规律运动，是最低成本的自律", "生活感悟"),
        Pair("体重下降的数字，是你日复一日坚持的勋章", "减脂语录")
    )
    private val _quoteState = MutableStateFlow<Pair<String, String>?>(null)
    val quoteState = _quoteState

    init {
        loadLocalQuote()
    }

    fun loadLocalQuote() {
        viewModelScope.launch {
            val randomItem = quoteList[Random.nextInt(quoteList.size)]
            _quoteState.emit(randomItem)
        }
    }

    // 计算BMI
    fun getBmiText(hStr: String, wFloat: Float): String {
        val h = hStr.toFloatOrNull() ?: 0f
        if (h <= 0f || wFloat <= 0f) return "未设置身高/暂无记录"
        val hM = h / 100f
        val bmi = wFloat / (hM * hM)
        return String.format("%.1f", bmi)
    }

    // 数据库操作：新增/删除体重记录
    fun deleteSingleRecord(record: WeightRecordEntity) {
        viewModelScope.launch {
            weightRepo.deleteRecord(record)
        }
    }
    fun insertRecord(record: WeightRecordEntity) {
        viewModelScope.launch {
            weightRepo.insertRecord(record)
        }
    }

    // 保存完整用户档案（适配修改后的DataStore，新增birthDate参数）
    fun saveProfile(height: String, targetW: String, genderStr: String, birthDateStr: String) {
        viewModelScope.launch {
            prefsRepo.saveUserProfile(height, targetW, genderStr, birthDateStr)
        }
    }

    // 深色模式切换
    fun switchDarkMode(enable: Boolean) {
        viewModelScope.launch {
            prefsRepo.setDarkMode(enable)
        }
    }

    // 根据出生日期自动计算周岁年龄
    fun calculateAge(birthDateStr: String): Int {
        if (birthDateStr.isBlank()) return 0
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val birth = format.parse(birthDateStr) ?: return 0
            val birthCal = Calendar.getInstance().apply { time = birth }
            val nowCal = Calendar.getInstance()
            var age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
            // 判断今年生日是否已过
            if (nowCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            if (age < 0) 0 else age
        } catch (e: Exception) {
            0
        }
    }
}