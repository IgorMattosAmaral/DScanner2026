package com.example.dscanner.util.dialogs

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.example.dscanner.data.local.entity.ProductEntity
import com.example.dscanner.ui.theme.Black
import com.example.dscanner.ui.theme.Orange
import com.example.dscanner.ui.theme.White
import com.example.dscanner.util.formatCurrencyInput

@Composable
fun PriceEditDialog(
    rawInput: String,
    onInputChange: (String) -> Unit,
    product: ProductEntity,
    onSave: (ProductEntity) -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        backgroundColor = Black,

        title = {
            Text(
                text = "Editar Preço",
                color = White
            )
        },

        text = {
            OutlinedTextField(
                value = formatCurrencyInput(rawInput),

                onValueChange = {
                    onInputChange(it.filter { ch -> ch.isDigit() })
                },

                label = {
                    Text("Novo Preço")
                },

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = White,
                    cursorColor = Orange,
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = White,
                    focusedLabelColor = Orange,
                    unfocusedLabelColor = White
                )
            )
        },

        confirmButton = {
            TextButton(
                onClick = {
                    val parsed = rawInput.toDoubleOrNull()?.div(100)

                    if (parsed != null) {
                        onSave(product.copy(price = parsed))
                    }
                }
            ) {
                Text(
                    text = "Salvar",
                    color = Orange
                )
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = Color.LightGray
                )
            }
        }
    )
}