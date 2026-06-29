package com.example.weighttrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weighttrack.ui.components.BottomNavBar
import com.example.weighttrack.ui.components.RecordItemCard
import com.example.weighttrack.viewmodel.StatisticsViewModel

@Composable
fun HomeScreen(
    viewModel: StatisticsViewModel,
    navController: NavController
) {
    val recordList = viewModel.allRecords.collectAsState(initial = emptyList()).value

    // 删除弹窗状态
    var showDeleteDialog by remember { mutableStateOf(false) }
    var targetDeleteRecord by remember { mutableStateOf<com.example.weighttrack.data.entity.WeightRecordEntity?>(null) }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("editRecord") }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {

            Text(
                text = "每日最新体重",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 32.dp)
            )

            // 加分项1：空数据提示
            if (recordList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "暂无体重记录",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                    )
                    Text(
                        text = "点击右下角加号添加第一条记录",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(recordList) { record ->
                        RecordItemCard(
                            record = record,
                            onLongClick = {
                                // 长按卡片，打开删除弹窗
                                targetDeleteRecord = record
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }

        // 加分项2：删除二次确认弹窗
        if (showDeleteDialog && targetDeleteRecord != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("删除记录") },
                text = { Text("确定要删除这条体重记录吗？删除后无法恢复！") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteSingleRecord(targetDeleteRecord!!)
                        showDeleteDialog = false
                        targetDeleteRecord = null
                    }) {
                        Text("确认删除", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}