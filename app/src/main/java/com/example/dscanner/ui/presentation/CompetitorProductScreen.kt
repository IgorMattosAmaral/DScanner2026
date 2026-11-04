package com.example.dscanner.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dscanner.data.local.entity.ProductEditEntity
import com.example.dscanner.ui.theme.Black
import com.example.dscanner.ui.theme.Brown
import com.example.dscanner.ui.theme.Orange
import com.example.dscanner.ui.theme.White
import com.example.dscanner.ui.viewmodel.CompetitorsViewModel
import com.example.dscanner.util.dialogs.ExportDialog
import exportToExcel

@Composable
fun CompetitorProductsScreen(
    competitorId: Long,
    navController: NavHostController,
    viewModel: CompetitorsViewModel = hiltViewModel()
) {
    var showPriceDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ProductEditEntity?>(null) }
    var rawInput by remember { mutableStateOf("0.00") }
    var showExportDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val competitorWithProductsState by viewModel.getCompetitorWithProductsFlow(competitorId)
        .collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Black,
                contentColor = White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Orange)
                    }
                },
                title = {
                    Text(
                        text = competitorWithProductsState?.competitor?.name ?: "Carregando...",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = White
                    )
                },
                actions = {
                    val count = competitorWithProductsState?.products?.size ?: 0
                    Text(
                        text = "$count produtos",
                        color = Orange,
                        modifier = Modifier.padding(end = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        viewModel.clearProductsFromCompetitor(competitorId)
                    }) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Limpar todos os produtos",
                            tint = Color.Red
                        )
                    }
                }
            )
        },
        backgroundColor = Black,
        floatingActionButton = {
            if (competitorWithProductsState != null) {
                FloatingActionButton(
                    onClick = { showExportDialog = true },
                    backgroundColor = Orange
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = "Exportar Excel",
                        tint = Black
                    )
                }
            }
        }
    ) { padding ->
        competitorWithProductsState?.let { competitorWithProducts ->
            LazyColumn(
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black)
            ) {
                items(competitorWithProducts.products) { product ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Brown,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = product.name,
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.weight(1f)
                                )

                                Row {
                                    IconButton(onClick = {
                                        editingProduct = product
                                        rawInput = String.format("%.2f", product.price)
                                        showPriceDialog = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = Orange
                                        )
                                    }
                                    IconButton(onClick = {
                                        viewModel.deleteProduct(product.id)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Excluir",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Preço: R$ %.2f".format(product.price),
                                color = Orange,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Código de barras: ${product.barCode}",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Orange)
            }
        }
    } // FIM DO SCAFFOLD

    // ==========================================
    // DIÁLOGOS DECLARADOS FORA DO SCAFFOLD
    // ==========================================

    // Diálogo de edição de preço
    if (showPriceDialog && editingProduct != null) {
        AlertDialog(
            onDismissRequest = { showPriceDialog = false },
            title = { Text("Editar preço de ${editingProduct?.name}") },
            text = {
                OutlinedTextField(
                    value = rawInput,
                    onValueChange = { input ->
                        if (input.matches(Regex("^[0-9]*[.,]?[0-9]{0,2}\$"))) {
                            rawInput = input
                        }
                    },
                    label = { Text("Preço") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val price = rawInput.replace(',', '.').toDoubleOrNull()
                    if (price != null && editingProduct != null) {
                        val updatedProduct = editingProduct!!.copy(price = price)
                        viewModel.updateProductPrice(updatedProduct)
                    }
                    showPriceDialog = false
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPriceDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}