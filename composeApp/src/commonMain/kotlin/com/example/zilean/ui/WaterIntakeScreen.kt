package com.example.zilean.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zilean.data.FoodEntry
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun WaterIntakeScreen(
    waterEntries: List<FoodEntry>,
    onBack: () -> Unit,
    onDeleteEntry: (FoodEntry) -> Unit,
    onEditEntry: (FoodEntry) -> Unit,
) {
    var editingEntry by remember { mutableStateOf<FoodEntry?>(null) }

    editingEntry?.let { entry ->
        AddWaterDialog(
            initialAmount = entry.water.toString(),
            onDismiss = { editingEntry = null },
            onConfirm = { amount ->
                onEditEntry(entry.copy(water = amount))
                editingEntry = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Nazad")
            }
            Text("Unos vode", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (waterEntries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nema unosa vode za ovaj dan.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(waterEntries) { entry ->
                    WaterEntryRow(
                        entry = entry,
                        onEdit = { editingEntry = entry },
                        onDelete = { onDeleteEntry(entry) }
                    )
                }
            }
        }
    }
}

@Composable
fun WaterEntryRow(
    entry: FoodEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val localDateTime = Instant.fromEpochMilliseconds(entry.date).toLocalDateTime(TimeZone.currentSystemDefault())
    val timeString = "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.LocalDrink,
                contentDescription = null,
                tint = Color(0xFF34D399),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("${entry.water} ml", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row {
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
                        contentDescription = "Obriši",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
