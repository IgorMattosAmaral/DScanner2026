package com.example.dscanner.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dscanner.ui.viewmodel.ProductViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dscanner.ui.theme.Black
import com.example.dscanner.ui.theme.Orange
import com.example.dscanner.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceResearchScreen(
    productViewModel: ProductViewModel = hiltViewModel()
) {

    var query by remember { mutableStateOf("") }

    // Concorrente
    var showCreateCompetitorDialog by remember { mutableStateOf(false) }
    var newCompetitorName by remember { mutableStateOf("") }

    // Produto
    var showCreateProductDialog by remember { mutableStateOf(false) }
    var newProductName by remember { mutableStateOf("") }
    var newProductBarcode by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    val fixedProducts by productViewModel.fixedProducts.collectAsState()
    val competitors by productViewModel.competitors.collectAsState()
    val selectedId by productViewModel.selectedCompetitorId.collectAsState()
    val selectedCompetitorName =
        competitors.find { it.id == selectedId }?.name ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(16.dp)
    ) {

        Text(
            text = "PESQUISA DE PREÇO",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Orange,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar produto") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                unfocusedBorderColor = White,
                focusedLabelColor = Orange,
                unfocusedLabelColor = White,
                cursorColor = Orange,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Search,
                        null,
                        tint = Orange
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                ),
                onClick = {
                    showCreateProductDialog = true
                }
            ) {
                Text(
                    "+ Produto",
                    color = Black
                )
            }

            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                ),
                onClick = {
                    showCreateCompetitorDialog = true
                }
            ) {
                Text(
                    "+ Concorrente",
                    color = Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (competitors.isNotEmpty()) {
                    expanded = !expanded
                }
            }
        ) {

            OutlinedTextField(
                value = selectedCompetitorName.ifBlank {
                    "Selecione concorrente"
                },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = {
                    Text("Concorrente")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = White,
                    focusedLabelColor = Orange,
                    unfocusedLabelColor = White,
                    cursorColor = Orange,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                competitors.forEach { competitor ->

                    DropdownMenuItem(
                        text = {
                            Text(competitor.name)
                        },
                        onClick = {
                            productViewModel.selectCompetitor(
                                competitor.id
                            )
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            val filteredProducts = fixedProducts.filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                )
            }

            items(filteredProducts) { product ->

                ProductCard(
                    product = product.copy(price = 0.0)
                )
            }
        }
    }

    // DIALOG NOVO PRODUTO

    if (showCreateProductDialog) {

        AlertDialog(

            onDismissRequest = {
                showCreateProductDialog = false
            },

            title = {
                Text(
                    "Novo Produto",
                    color = White
                )
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = newProductName,
                        onValueChange = {
                            newProductName = it
                        },
                        label = {
                            Text("Nome")
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newProductBarcode,
                        onValueChange = {
                            newProductBarcode = it
                        },
                        label = {
                            Text("Código de Barras")
                        }
                    )
                }
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        if (newProductName.isNotBlank()) {

                            productViewModel.addProduct(
                                name = newProductName.trim(),
                                barCode = newProductBarcode.trim()
                            )

                            newProductName = ""
                            newProductBarcode = ""

                            showCreateProductDialog = false
                        }
                    }

                ) {
                    Text(
                        "Salvar",
                        color = Orange
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showCreateProductDialog = false
                        newProductName = ""
                        newProductBarcode = ""

                    }

                ) {
                    Text(
                        "Cancelar",
                        color = Color.LightGray
                    )
                }
            },

            containerColor = Black
        )
    }

    //=========================
    // DIALOG NOVO CONCORRENTE
    //=========================

    if (showCreateCompetitorDialog) {

        AlertDialog(

            onDismissRequest = {
                showCreateCompetitorDialog = false
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        if (newCompetitorName.isNotBlank()) {

                            productViewModel.createCompetitor(
                                newCompetitorName.trim()
                            )

                            newCompetitorName = ""
                            showCreateCompetitorDialog = false
                        }
                    }

                ) {
                    Text(
                        "Criar",
                        color = Orange
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        newCompetitorName = ""
                        showCreateCompetitorDialog = false

                    }

                ) {
                    Text(
                        "Cancelar",
                        color = Color.LightGray
                    )
                }
            },

            title = {

                Text(
                    "Novo Concorrente",
                    color = White
                )
            },

            text = {

                OutlinedTextField(
                    value = newCompetitorName,
                    onValueChange = {
                        newCompetitorName = it
                    },
                    label = {
                        Text("Nome do concorrente")
                    },
                    singleLine = true
                )
            },

            containerColor = Black
        )
    }
}




