package com.example.dscanner.util.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun BarcodeDialog(
    barcodeBitmap: ImageBitmap?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        },
        title = { Text("Código de Barras") },
        text = {
            if (barcodeBitmap != null) {
                Image(
                    bitmap = barcodeBitmap,
                    contentDescription = "Código de Barras",
                    modifier = Modifier
                        .height(300.dp)
                        .graphicsLayer(rotationZ = 90f)
                )
            } else {
                Text("Gerando imagem...")
            }
        }
    )
}
