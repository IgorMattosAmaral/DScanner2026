package com.example.dscanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dscanner.data.model.Screen
import com.example.dscanner.ui.theme.Black
import com.example.dscanner.ui.theme.Brown
import com.example.dscanner.ui.theme.Orange
import com.example.dscanner.ui.theme.White
import com.example.dscanner.ui.viewmodel.CompetitorsViewModel
import com.example.dscanner.util.dialogs.ExportDialog

@Composable
fun CompetitorsScreen(
    navController: NavHostController,
    viewModel: CompetitorsViewModel = hiltViewModel()
) {

    val competitors by viewModel.allCompetitorsFlow.collectAsState(initial = emptyList())

    // Será usado pelo ExportDialog
    val competitorsWithProducts by viewModel.competitorsWithProducts.collectAsState(
        initial = emptyList()
    )

    val context = LocalContext.current

    var showExportDialog by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Concorrentes",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange
                )

                Button(
                    onClick = {
                        showExportDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange
                    )
                ) {
                    Text(
                        "Exportar",
                        color = Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (competitors.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Nenhum concorrente cadastrado ainda.",
                        color = White
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        competitors,
                        key = { it.id }
                    ) { competitor ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Brown
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),

                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = competitor.name,
                                    color = White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {

                                            navController.navigate(
                                                Screen.CompetitorProducts.createRoute(
                                                    competitor.id
                                                )
                                            )

                                        }
                                )

                                IconButton(
                                    onClick = {
                                        viewModel.deleteCompetitor(
                                            competitor
                                        )
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Exportação de TODOS os concorrentes
    if (showExportDialog) {

        ExportDialog(

            competitors = competitorsWithProducts,

            context = context,

            onDismiss = {

                showExportDialog = false

            }

        )
    }
}


