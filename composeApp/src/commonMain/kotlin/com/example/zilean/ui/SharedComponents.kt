package com.example.zilean.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zilean.data.FoodEntry
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.datetime.*

@Composable
fun AddWaterDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    initialAmount: String = ""
) {
    var amount by remember { mutableStateOf(initialAmount) }
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(initialAmount) {
        amount = initialAmount
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Unesi količinu vode", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            amount = it
                            showError = false
                        }
                    },
                    isError = showError,
                    label = { Text("Količina (ml)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                if (showError) {
                    Text(
                        text = "Unesi ispravnu kolicinu.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val waterAmount = amount.toIntOrNull()
                    if (waterAmount != null && waterAmount > 0) {
                        onConfirm(waterAmount)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarHeader(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                        onDateChange(date)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Otkaži")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val dateString = "${selectedDate.dayOfMonth}. ${selectedDate.month.name} ${selectedDate.year}."

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousDay) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prethodni dan")
        }

        Text(
            text = dateString,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { showDatePicker = true }
        )

        IconButton(onClick = onNextDay) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Sledeći dan")
        }
    }
}

@Composable
fun FoodItemRow(entry: FoodEntry, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column (modifier = Modifier.weight(1f)){
                Text(text = entry.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${entry.calories} kcal | P:${entry.protein}g | C:${entry.carbs}g | F:${entry.fats}g",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier.padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Izmeni",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete, 
                        contentDescription = "Obriši",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
