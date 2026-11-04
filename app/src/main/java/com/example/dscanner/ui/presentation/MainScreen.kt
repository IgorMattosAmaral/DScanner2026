package com.example.dscanner.ui.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dscanner.data.model.Screen
import com.example.dscanner.data.navigation.BottomNavigationBar
import com.example.dscanner.ui.screens.CompetitorsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.PriceResearch.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tela principal de pesquisa (sem argumento)
            composable(Screen.PriceResearch.route) {
                PriceResearchScreen()
            }

            // Tela de pesquisa com ID do concorrente (argumento obrigatório)
            composable(
                route = Screen.PriceResearchWithId.route,
            ) {
                PriceResearchScreen()
            }

            // Tela de lista de concorrentes (SEM argumento, pois a rota "competitors" não tem parâmetro)
            composable(Screen.CompetitorsScreen.route) {
                CompetitorsScreen(
                    navController = navController,
                )
            }

            // Tela de produtos do concorrente (argumento obrigatório)
            composable(
                route = Screen.CompetitorProducts.route,
                arguments = listOf(navArgument("competitorId") { type = NavType.LongType })
            ) { backStackEntry ->
                val competitorId = backStackEntry.arguments?.getLong("competitorId") ?: 0L
                CompetitorProductsScreen(
                    competitorId = competitorId,
                    navController = navController
                )
            }
        }
    }
}



