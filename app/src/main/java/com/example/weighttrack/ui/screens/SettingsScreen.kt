package com.example.weighttrack.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weighttrack.ui.components.BottomNavBar
import com.example.weighttrack.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    // 读取持久化数据
    val heightState = viewModel.height.collectAsState("")
    val targetWState = viewModel.targetWeight.collectAsState("")
    val genderState = viewModel.gender.collectAsState("")
    val birthDateState = viewModel.birthDate.collectAsState("")
    val darkModeState = viewModel.darkMode.collectAsState(false)

    // 本地输入状态
    var heightInput by remember { mutableStateOf(heightState.value) }
    var targetWInput by remember { mutableStateOf(targetWState.value) }
    var genderInput by remember { mutableStateOf(genderState.value) }
    var birthDateInput by remember { mutableStateOf(birthDateState.value) }
    var darkSwitch by remember { mutableStateOf(darkModeState.value) }

    var showGenderDialog by remember { mutableStateOf(false) }
    // 空白交互源，去除点击水波纹
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(bottomBar = { BottomNavBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text("个人档案设置", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = heightInput,
                onValueChange = { heightInput = it },
                label = { Text("身高(cm)") },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )

            OutlinedTextField(
                value = targetWInput,
                onValueChange = { targetWInput = it },
                label = { Text("目标体重(kg)") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )

            // 修复：移除非法onClick参数，改用Modifier.clickable实现点击弹窗
            OutlinedTextField(
                value = genderInput,
                onValueChange = {},
                label = { Text("性别") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        showGenderDialog = true
                    },
                readOnly = true,
                // 禁用光标，完全不可输入
                enabled = false
            )

            // 出生日期输入框
            OutlinedTextField(
                value = birthDateInput,
                onValueChange = { birthDateInput = it },
                label = { Text("出生日期 (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                singleLine = true
            )

            Button(
                modifier = Modifier.padding(top = 20.dp),
                onClick = {
                    viewModel.saveProfile(heightInput, targetWInput, genderInput, birthDateInput)
                }
            ) {
                Text("保存档案")
            }

            Column(modifier = Modifier.padding(top = 30.dp)) {
                Text("主题设置")
                Switch(
                    checked = darkSwitch,
                    onCheckedChange = {
                        darkSwitch = it
                        viewModel.switchDarkMode(it)
                    }
                )
                Text("深色模式")
            }
        }
    }

    // 性别选择弹窗（Composable上下文内，无报错）
    if (showGenderDialog) {
        AlertDialog(
            onDismissRequest = { showGenderDialog = false },
            title = { Text("请选择性别") },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showGenderDialog = false }) {
                    Text("取消")
                }
            },
            text = {
                Column {
                    TextButton(onClick = {
                        genderInput = "男"
                        showGenderDialog = false
                    }) {
                        Text("男", style = MaterialTheme.typography.titleMedium)
                    }
                    TextButton(onClick = {
                        genderInput = "女"
                        showGenderDialog = false
                    }) {
                        Text("女", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        )
    }
}