package com.example.zilean.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    currentCalories: Int,
    currentProtein: Int,
    currentCarbs: Int,
    currentFats: Int,
    currentWater: Int,
    onSaveGoals: (Int, Int, Int, Int, Int) -> Unit,
    onBack: () -> Unit
) {
    var calories by remember { mutableStateOf(currentCalories.toString()) }
    var protein by remember { mutableStateOf(currentProtein.toString()) }
    var carbs by remember { mutableStateOf(currentCarbs.toString()) }
    var fats by remember { mutableStateOf(currentFats.toString()) }
    var water by remember { mutableStateOf(currentWater.toString()) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
            }
            Text("Ciljevi", style = MaterialTheme.typography.headlineMedium)
        }

        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Cilj kalorija (kcal)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Cilj proteina (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Cilj hidrata (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = fats,
            onValueChange = { fats = it },
            label = { Text("Cilj masti (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = water,
            onValueChange = { water = it },
            label = { Text("Cilj vode (ml)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val cal = calories.toIntOrNull() ?: currentCalories
                val prot = protein.toIntOrNull() ?: currentProtein
                val carb = carbs.toIntOrNull() ?: currentCarbs
                val fat = fats.toIntOrNull() ?: currentFats
                val wat = water.toIntOrNull() ?: currentWater
                onSaveGoals(cal, prot, carb, fat, wat)
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp)
        ) {
            Text("SACUVAJ")
        }
    }
}
