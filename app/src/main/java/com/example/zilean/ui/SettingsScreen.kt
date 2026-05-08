package com.example.zilean.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun SettingsScreen(
    name: String?,
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onBack: () -> Unit,
    onOpenGoals: () -> Unit,
    onOpenScannedProducts: () -> Unit,
    onUpdateName: (String) -> Unit
) {
    var showEditUserDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(name ?: "") }

    if (showEditUserDialog) {
        AlertDialog(
            onDismissRequest = { showEditUserDialog = false },
            title = { Text("Izmeni ime") },
            text = {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Ime") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    onUpdateName(editedName)
                    showEditUserDialog = false
                }) {
                    Text("Sacuvaj")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditUserDialog = false }) {
                    Text("Otkazi")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
            }
            Text("Podesavanja", style = MaterialTheme.typography.headlineMedium)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {

            item {
                SettingsSectionTitle("Korisnicki podaci")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { showEditUserDialog = true },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Ime: $name", fontWeight = FontWeight.Bold)
                        Text(text = "Klikni za izmenu", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onOpenGoals() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Ciljevi", fontWeight = FontWeight.Bold)
                        Text(text = "Kalorije, makronutrijenti, voda", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onOpenScannedProducts() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Skenirani proizvodi", fontWeight = FontWeight.Bold)
                        Text(text = "Upravljaj skeniranim artiklima", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                SettingsSectionTitle("Izgled")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tamni rezim")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { newValue ->
                            onThemeToggle(newValue)
                        },
                        colors = SwitchDefaults.colors(MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    }

}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}