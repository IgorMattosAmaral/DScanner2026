package com.example.dscanner.data.navigation


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Modifier
import com.example.dscanner.data.model.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val Orange = Color(0xFFFF9800)
    val Black = Color(0xFF121212)
    val White = Color(0xFFFFFFFF)

    val screens = listOf(
        Screen.PriceResearch,
        Screen.CompetitorsScreen
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        backgroundColor = Black, // Cor de fundo da barra
        contentColor = White // Cor padrão de conteúdo (ícones e texto)
    ) {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                selectedContentColor = Orange,
                unselectedContentColor = White.copy(alpha = 0.6f),
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
