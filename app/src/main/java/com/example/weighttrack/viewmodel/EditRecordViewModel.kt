package com.example.weighttrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import com.example.weighttrack.data.entity.WeightRecordEntity
import com.example.weighttrack.data.repository.WeightRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditRecordViewModel(
    private val repo: WeightRepository
) : ViewModel() {
    // 体重输入状态
    var weightInput = mutableStateOf("")
        private set

    // 日期输入状态：初始化自动填充今日本地日期
    var dateInput = mutableStateOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    )
        private set

    // 错误提示
    var errorTip = mutableStateOf("")
        private set

    // 更新体重输入
    fun updateWeight(text: String) {
        weightInput.value = text
        errorTip.value = "" // 输入时清空错误提示
    }

    // 更新日期输入（用户手动修改日期时调用）
    fun updateDate(text: String) {
        dateInput.value = text
        errorTip.value = ""
    }

    // 保存记录：做校验 + 插入数据库
    fun saveRecord(onSuccess: () -> Unit) {
        val weightNum = weightInput.value.toFloatOrNull()
        val dateStr = dateInput.value.trim()

        // 原有基础校验
        when {
            weightInput.value.isBlank() -> {
                errorTip.value = "体重不能为空"
                return
            }
            weightNum == null -> {
                errorTip.value = "请输入合法数字"
                return
            }
            weightNum <= 0f -> {
                errorTip.value = "体重必须大于0"
                return
            }
            weightNum > 300f -> {
                errorTip.value = "体重数值超出合理范围"
                return
            }
            dateStr.isBlank() -> {
                errorTip.value = "日期不能为空"
                return
            }
        }

        // 新增：日期格式 + 未来日期校验
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            isLenient = false // 严格校验，非法日期（2月30/13月）直接报错
        }
        val todayDate = Calendar.getInstance().time

        try {
            val inputParseDate = dateFormat.parse(dateStr)
            // 判断是否是未来日期（输入日期 > 今天）
            if (inputParseDate.after(todayDate)) {
                errorTip.value = "日期无效，请输入有效日期"
                return
            }
        } catch (e: Exception) {
            // 格式不对 / 不存在的日期 都会进入这里
            errorTip.value = "日期格式错误，请使用 yyyy-MM-dd"
            return
        }

        // 全部校验通过，插入数据库
        viewModelScope.launch {
            // 修复点：weightNum!! 强制转为非空Float，消除类型不匹配报错
            val newRecord = WeightRecordEntity(date = dateStr, weight = weightNum!!)
            repo.insertRecord(newRecord)
            onSuccess() // 保存成功回调，返回上一页
        }
    }
}