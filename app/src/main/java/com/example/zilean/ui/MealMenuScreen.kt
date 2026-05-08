package com.example.zilean.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zilean.data.MealPreset

@Composable
fun MealMenuScreen(
    presets: List<MealPreset>,
    onPresetSelected: (MealPreset) -> Unit,
    onBack: () -> Unit,
    onDeletePreset: (MealPreset) -> Unit,
    viewModel: FoodViewModel,
) {
    var editingPreset by remember { mutableStateOf<MealPreset?>(null) }

    editingPreset?.let { preset ->
        AddFoodDialog(
            initialName = preset.name,
            initialProtein = preset.protein.toString(),
            initialCalories = preset.calories.toString(),
            initialCarbs = preset.carbs.toString(),
            initialFats = preset.fats.toString(),
            showSavePresetOption = false,
            onDismiss = { editingPreset = null },
            onConfirm = { name, prot, cal, carb, fat, _ ->
                viewModel.editPreset(preset.copy(
                    name = name,
                    protein = prot,
                    calories = cal,
                    carbs = carb,
                    fats = fat
                ))
                editingPreset = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
            }
            Text("Moj Jelovnik", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(presets) { preset ->
                MealPresetRow(
                    preset = preset,
                    onSelect = { onPresetSelected(preset) },
                    onEdit = { editingPreset = preset },
                    onDelete = { onDeletePreset(preset) }
                )
            }
        }
    }
}

@Composable
fun MealPresetRow(
    preset: MealPreset,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(preset.name, fontWeight = FontWeight.Bold)
                Text(
                    text = "${preset.calories} kcal | P: ${preset.protein}g | C: ${preset.carbs}g | F: ${preset.fats}g",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Izmeni",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Obrisi",
                    tint = Color.Red
                )
            }
        }
    }
}