package com.example.dscanner.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dscanner.ui.viewmodel.ProductViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.dscanner.data.local.entity.ProductEntity
import com.example.dscanner.data.model.ProductCardState
import com.example.dscanner.util.dialogs.BarcodeDialog
import com.example.dscanner.util.dialogs.PriceEditDialog
import com.example.dscanner.util.sanitizeEAN13

@Composable
fun ProductCard(
    product: ProductEntity,
    productViewModel: ProductViewModel = hiltViewModel(),
) {
    var state by remember { mutableStateOf(ProductCardState()) }

    val selectedCompetitorId by productViewModel.selectedCompetitorId.collectAsState()
    val barcodeBitmap = productViewModel.barCodeImage.value

    // Armazena o preço editado temporariamente
    var editedPrice by remember { mutableStateOf(product.price) }

    //Usa o concorrente selecionado no ProductViewModel
    fun handleSavePrice(updatedProduct: ProductEntity) {
        editedPrice = updatedProduct.price
        if (selectedCompetitorId != null) {
            productViewModel.saveProductForCompetitor(
                product = updatedProduct,
                price = editedPrice,
                competitorId = selectedCompetitorId!!
            )
        } else {
            println("Nenhum concorrente selecionado")
        }

        state = state.copy(
            showPriceDialog = false,
            rawInput = "0"
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        try {
                            val sanitized = sanitizeEAN13(product.barCode)
                            productViewModel.createBarCodeImage(sanitized)
                            state = state.copy(showBarcodeDialog = true)
                        } catch (e: IllegalArgumentException) {
                            println("Erro no código EAN: ${e.message}")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Código de Barras", tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Barras", color = Color.Black)
                }

                Button(
                    onClick = {
                        state = state.copy(showPriceDialog = true)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Preço", tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar Preço", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Preço: R$ %.2f".format(product.price),
                color = Color(0xFFFF9800),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

    // Diálogo código de barras
    if (state.showBarcodeDialog) {
        BarcodeDialog(
            barcodeBitmap = barcodeBitmap?.asImageBitmap(),
            onDismiss = {
                state = state.copy(showBarcodeDialog = false)
            }
        )
    }

    // ✅ MANTIDO: Diálogo de edição de preço
    if (state.showPriceDialog) {
        PriceEditDialog(
            rawInput = state.rawInput,
            onInputChange = { input ->
                state = state.copy(rawInput = input)
            },
            product = product,
            onSave = { updatedProduct ->
                handleSavePrice(updatedProduct)
            },
            onDismiss = {
                state = state.copy(
                    rawInput = "0",
                    showPriceDialog = false
                )
            }
        )
    }
}
