package com.example.weighttrack.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weighttrack.viewmodel.EditRecordViewModel

@Composable
fun EditRecordScreen(
    viewModel: EditRecordViewModel,
    navController: NavController
) {
    Scaffold(modifier = Modifier.padding(20.dp)) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "添加体重记录",
                style = MaterialTheme.typography.headlineMedium
            )

            // ========== 新增：日期输入框（默认今日，可手动修改） ==========
            OutlinedTextField(
                value = viewModel.dateInput.value,
                onValueChange = { viewModel.updateDate(it) },
                label = { Text("记录日期 (yyyy-MM-dd)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                singleLine = true,
                isError = viewModel.errorTip.value.isNotEmpty() && viewModel.dateInput.value.isBlank()
            )

            // 体重输入框（绑定ViewModel状态）
            OutlinedTextField(
                value = viewModel.weightInput.value,
                onValueChange = { viewModel.updateWeight(it) },
                label = { Text("输入体重(kg)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                singleLine = true,
                isError = viewModel.errorTip.value.isNotEmpty(),
                supportingText = {
                    if (viewModel.errorTip.value.isNotEmpty()) {
                        Text(viewModel.errorTip.value)
                    }
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = {
                    viewModel.saveRecord {
                        navController.popBackStack()
                    }
                    // 如果有错误，弹出AlertDialog
                    if (viewModel.errorTip.value.isNotEmpty()) {
                        // 弹窗逻辑自行添加，当前方案底部文字更简洁
                    }
                }
            ) {
                Text("保存记录")
            }
        }
    }
}