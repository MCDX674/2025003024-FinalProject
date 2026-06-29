package com.example.weighttrack.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weighttrack.ui.components.BottomNavBar
import com.example.weighttrack.ui.components.QuoteCard
import com.example.weighttrack.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    navController: NavController
) {
    val latestState = viewModel.latestRecord.collectAsState(initial = null)
    val countState = viewModel.recordCount.collectAsState(initial = 0)
    val heightState = viewModel.height.collectAsState(initial = "")
    val targetWState = viewModel.targetWeight.collectAsState(initial = "")
    val genderState = viewModel.gender.collectAsState(initial = "")
    val birthDateState = viewModel.birthDate.collectAsState(initial = "")
    val quote = viewModel.quoteState.collectAsState(initial = null).value

    val latest = latestState.value
    val count = countState.value
    val heightStr = heightState.value
    val targetWStr = targetWState.value
    val genderStr = genderState.value
    val birthDateStr = birthDateState.value

    val latestWeight = latest?.weight ?: 0f
    val targetFloat = targetWStr.toFloatOrNull() ?: 0f
    val age = viewModel.calculateAge(birthDateStr)
    val bmiText = viewModel.getBmiText(heightStr, latestWeight)

    Scaffold(bottomBar = { BottomNavBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // 页面标题
            Text("数据信息", style = MaterialTheme.typography.headlineMedium)

            // 健康励志语录（移除报错的modifier参数，外边加padding）
            Column(modifier = Modifier.padding(top = 24.dp)) {
                QuoteCard(quote = quote)
            }

            // 数据项顺序：性别、年龄、身高、最新体重、目标体重、BMI、记录总数
            Text(
                text = "性别：${genderStr.ifBlank { "未设置" }}",
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "年龄：${if (age > 0) "$age 岁" else "未设置出生日期"}",
                modifier = Modifier.padding(top = 14.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "身高：${if (heightStr.isNotBlank()) "$heightStr cm" else "未设置"}",
                modifier = Modifier.padding(top = 14.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "最新体重：${if (latestWeight > 0) "$latestWeight kg" else "暂无记录"}",
                modifier = Modifier.padding(top = 14.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "目标体重：${if (targetFloat > 0) "$targetFloat kg" else "未设置"}",
                modifier = Modifier.padding(top = 14.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "BMI 指数：$bmiText",
                modifier = Modifier.padding(top = 14.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "记录总数：$count 条",
                modifier = Modifier.padding(top = 14.dp),
                style = MaterialTheme.typography.titleLarge
            )

            // 增重/减重提示，两项数据都存在才显示
            if (latestWeight > 0f && targetFloat > 0f) {
                val weightDiff = latestWeight - targetFloat
                val tipText = when {
                    weightDiff > 0f -> "距离目标还需减重 %.1f kg".format(weightDiff)
                    weightDiff < 0f -> "距离目标还需增重 %.1f kg".format(-weightDiff)
                    else -> "恭喜你，已达成目标体重！"
                }
                Text(
                    text = tipText,
                    modifier = Modifier.padding(top = 20.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}