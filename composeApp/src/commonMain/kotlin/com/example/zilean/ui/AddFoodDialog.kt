package com.example.zilean.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddFoodDialog(
    initialName: String = "",
    initialProtein: String = "",
    initialCalories: String = "",
    initialCarbs: String = "",
    initialFats: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, Int, Int, Boolean) -> Unit,
    showSavePresetOption: Boolean = true,
) {
    var name by remember { mutableStateOf(initialName) }
    var protein by remember { mutableStateOf(initialProtein) }
    var calories by remember { mutableStateOf(initialCalories) }
    var carbs by remember { mutableStateOf(initialCarbs) }
    var fats by remember { mutableStateOf(initialFats) }

    var saveAsPreset by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(initialName, initialProtein) {
        name = initialName
        protein = initialProtein
        calories = initialCalories
        carbs = initialCarbs
        fats = initialFats
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Unesi obrok", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it
                                    if (it.isNotBlank()) showError = false},
                    isError = showError && name.isBlank(),
                    label = { Text("Naziv hrane") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = calories,
                    onValueChange = { if (it.all { char -> char.isDigit() }) calories = it
                        if (it.isNotBlank()) showError = false},
                    isError = showError && calories.isBlank(),
                    label = { Text("Kalorije (kcal)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = protein,
                    onValueChange = { if (it.all { char -> char.isDigit() }) protein = it
                        if (it.isNotBlank()) showError = false},
                    isError = showError && protein.isBlank(),
                    label = { Text("Proteini (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = carbs,
                    onValueChange = { if (it.all { char -> char.isDigit() }) carbs = it
                        if (it.isNotBlank()) showError = false},
                    isError = showError && carbs.isBlank(),
                    label = { Text("Ugljeni hidrati (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fats,
                    onValueChange = { if (it.all { char -> char.isDigit() }) fats = it
                        if (it.isNotBlank()) showError = false},
                    isError = showError && fats.isBlank(),
                    label = { Text("Masti (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (showSavePresetOption) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = saveAsPreset,
                            onCheckedChange = { saveAsPreset = it }
                        )
                        Text("Sacuvaj u jelovnik")
                    }
                }

                if (showError) {
                    Text(
                        text = "Sva polja su obavezna.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && protein.isNotBlank() && calories.isNotBlank() && carbs.isNotBlank() && fats.isNotBlank()) {
                        onConfirm(
                            name,
                            protein.toIntOrNull() ?: 0,
                            calories.toIntOrNull() ?: 0,
                            carbs.toIntOrNull() ?: 0,
                            fats.toIntOrNull() ?: 0,
                            saveAsPreset
                        )
                    } else {
                        showError = true
                    }
                }
            ) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Otkazi")
            }
        }
    )
}

@Composable
fun ErrorText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
    )
}