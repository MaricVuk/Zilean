package com.example.zilean.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun AddFoodDialog(
    initialName: String = "",
    initialProtein: String = "",
    initialCalories: String = "",
    initialCarbs: String = "",
    initialFats: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, Int, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var carbs by remember {mutableStateOf("")}
    var fats by remember {mutableStateOf("")}
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
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it
                        if (it.isNotBlank()) showError = false
                                    },
                    isError = showError && name.isBlank(),
                    label = { Text("Naziv hrane (npr. Jaja)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (showError && name.isBlank()) ErrorText("Unesi ime hrane")



                OutlinedTextField(
                    value = calories,
                    onValueChange = { if (it.all { char -> char.isDigit() }) calories = it },
                    isError = showError && calories.isBlank(),
                    label = { Text("Kalorije (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (showError && name.isBlank()) ErrorText("Unesi kalorije")


                OutlinedTextField(
                    value = protein,
                    onValueChange = { if (it.all { char -> char.isDigit() }) protein = it },
                    isError = showError && protein.isBlank(),

                    label = { Text("Proteini (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError && name.isBlank()) ErrorText("Unesi proteine")
                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = carbs,
                    onValueChange = { if (it.all { char -> char.isDigit() }) carbs = it },
                    isError = showError && carbs.isBlank(),

                    label = { Text("Hidrati (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError && name.isBlank()) ErrorText("Unesi hidrate")
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fats,
                    onValueChange = { if (it.all { char -> char.isDigit() }) fats = it },
                    isError = showError && fats.isBlank(),
                    label = { Text("Masti (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError && name.isBlank()) ErrorText("Unesi masti")

            }
        },
        confirmButton = {
            Button(onClick = {
                val isValid = name.isNotBlank() && protein.isNotBlank() &&
                        calories.isNotBlank() && carbs.isNotBlank() && fats.isNotBlank()

                if (isValid) {
                    onConfirm(
                        name,
                        protein.toInt(),
                        calories.toInt(),
                        carbs.toInt(),
                        fats.toInt()
                    )
                    showError = false
                } else {
                    showError = true
                }
            }) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Otkaži")
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