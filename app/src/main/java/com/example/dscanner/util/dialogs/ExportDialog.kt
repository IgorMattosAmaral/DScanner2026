package com.example.dscanner.util.dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.dscanner.data.local.entity.CompetitorWithProducts
import exportAllToExcel
import shareFile

@Composable
fun ExportDialog(
    competitors: List<CompetitorWithProducts>,
    context: Context,
    onDismiss: () -> Unit
) {


    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Exportar pesquisa de preços")
        },

        text = {
            Text("Deseja salvar ou compartilhar a pesquisa completa?")
        },

        confirmButton = {

            TextButton(

                onClick = {

                    val file = exportAllToExcel(
                        context = context,
                        competitors = competitors
                    )

                    if (file != null) {
                        Toast.makeText(
                            context,
                            "Arquivo salvo em: ${file.absolutePath}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    onDismiss()
                }

            ) {
                Text("Salvar")
            }
        },

        dismissButton = {

            TextButton(

                onClick = {

                    val file = exportAllToExcel(
                        context = context,
                        competitors = competitors
                    )

                    if (file != null) {
                        shareFile(context, file)
                    }

                    onDismiss()
                }

            ) {
                Text("Compartilhar")
            }
        }
    )
}