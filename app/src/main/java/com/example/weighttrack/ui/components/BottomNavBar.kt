package com.example.weighttrack.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.weighttrack.navigation.Screen

// 手动定义页面名称
val navItems = listOf(
    Screen.Home to "home",
    Screen.Statistics to "statistics",
    Screen.Settings to "settings"
)

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        navItems.forEach { (screen, name) ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                label = { Text(name) },
                icon = {}
            )
        }
    }
}