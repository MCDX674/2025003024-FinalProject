package com.example.weighttrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weighttrack.WeightTrackApp
import com.example.weighttrack.ui.screens.EditRecordScreen
import com.example.weighttrack.ui.screens.HomeScreen
import com.example.weighttrack.ui.screens.SettingsScreen
import com.example.weighttrack.ui.screens.StatisticsScreen
import com.example.weighttrack.viewmodel.EditRecordViewModel
import com.example.weighttrack.viewmodel.EditRecordViewModelFactory
import com.example.weighttrack.viewmodel.SettingsViewModel
import com.example.weighttrack.viewmodel.SettingsViewModelFactory
import com.example.weighttrack.viewmodel.StatisticsViewModel
import com.example.weighttrack.viewmodel.StatisticsViewModelFactory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
    object EditRecord : Screen("editRecord")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        // 首页（保持不变）
        composable(Screen.Home.route) {
            val appCtx = LocalContext.current.applicationContext as WeightTrackApp
            val statVm: StatisticsViewModel = viewModel(
                factory = StatisticsViewModelFactory(appCtx.weightRepository, appCtx.userPrefsRepository)
            )
            HomeScreen(
                viewModel = statVm,
                navController = navController
            )
        }

        // 统计页（保持不变）
        composable(Screen.Statistics.route) {
            val appCtx = LocalContext.current.applicationContext as WeightTrackApp
            val statVm: StatisticsViewModel = viewModel(
                factory = StatisticsViewModelFactory(appCtx.weightRepository, appCtx.userPrefsRepository)
            )
            StatisticsScreen(
                viewModel = statVm,
                navController = navController
            )
        }

        // ========== 修改：设置页改用 SettingsViewModel ==========
        composable(Screen.Settings.route) {
            val appCtx = LocalContext.current.applicationContext as WeightTrackApp
            val settingsVm: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(appCtx.userPrefsRepository)
            )
            SettingsScreen(
                viewModel = settingsVm,
                navController = navController
            )
        }

        // ========== 修改：添加体重页改用 EditRecordViewModel ==========
        composable(Screen.EditRecord.route) {
            val appCtx = LocalContext.current.applicationContext as WeightTrackApp
            val editVm: EditRecordViewModel = viewModel(
                factory = EditRecordViewModelFactory(appCtx.weightRepository)
            )
            EditRecordScreen(
                viewModel = editVm,
                navController = navController
            )
        }
    }
}