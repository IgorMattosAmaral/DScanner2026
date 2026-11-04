package com.example.dscanner.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object PriceResearch : Screen("price_research", "Pesquisa", Icons.Default.Search)

    object PriceResearchWithId : Screen("price_research_with_id/{competitorId}", "Pesquisa", Icons.Default.Search) {
        fun createRoute(competitorId: Long) = "price_research_with_id/$competitorId"
    }

    object CompetitorProducts : Screen("competitor_products/{competitorId}", "Produtos", Icons.Default.Info) {
        fun createRoute(competitorId: Long) = "competitor_products/$competitorId"
    }

    object CompetitorsScreen : Screen("competitors", "Concorrentes", Icons.Default.Info)
}