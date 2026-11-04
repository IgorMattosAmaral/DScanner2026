package com.example.dscanner.util.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dscanner.data.local.entity.CompetitorsEntity
import com.example.dscanner.ui.theme.Black
import com.example.dscanner.ui.theme.Orange
import com.example.dscanner.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun CompetitorSelectDialog(
    competitors: List<CompetitorsEntity>,
    newName: String,
    onNewNameChange: (String) -> Unit,
    onSelect: suspend (CompetitorsEntity) -> Unit,
    onCreateAndSelect: suspend (String) -> Unit,
    onDismiss: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Black,

        title = {
            Text(
                text = "Selecione o Concorrente",
                color = White
            )
        },

        text = {
            Column {

                if (competitors.isNotEmpty()) {

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {

                        items(competitors) { competitor ->

                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    coroutineScope.launch {
                                        onSelect(competitor)
                                        onDismiss()
                                    }
                                }
                            ) {
                                Text(
                                    text = competitor.name,
                                    color = Orange
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))
                } else {

                    Text(
                        text = "Nenhum concorrente cadastrado.",
                        color = White
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = newName,
                    onValueChange = onNewNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Nome do novo concorrente")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        disabledTextColor = White,

                        focusedBorderColor = Orange,
                        unfocusedBorderColor = White,

                        focusedLabelColor = Orange,
                        unfocusedLabelColor = White,

                        cursorColor = Orange
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (newName.isNotBlank()) {
                            coroutineScope.launch {
                                onCreateAndSelect(newName.trim())
                                onDismiss()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Criar Novo Concorrente",
                        color = Black
                    )
                }
            }
        },

        confirmButton = {},

        dismissButton = {}
    )
}