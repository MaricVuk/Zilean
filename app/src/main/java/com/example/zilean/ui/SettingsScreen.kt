package com.example.zilean.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zilean.data.ProductMetadata


@Composable
fun SettingsScreen(
    name: String?,
    //currentGoals: Map<String, Int>,
    //scannedProducts: List<ProductMetadata>,
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    //onUpdateUserInfo: (String, Int, Int, Int, Int, Int, Int) -> Unit,
    onBack: () -> Unit
) {
    //val scannedProducts by viewModel.allProducts.collectAsState(initial = emptyList())
    var showEditUserDialog by remember { mutableStateOf(false) }

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
                    modifier = Modifier.fillMaxWidth().clickable { showEditUserDialog = true },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Ime: $name", fontWeight = FontWeight.Bold)
                        //Text("Cilj Proteina: ${currentGoals["prot"]}g")
                        //Text("Cilj Kalorija: ${currentGoals["cal"]} kcal")
                        Text(text = "Klikni za izmenu", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
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
                    Text("Tamni režim")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { newValue ->
                            onThemeToggle(newValue)
                        },
                        colors = SwitchDefaults.colors(MaterialTheme.colorScheme.surface)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                //SettingsSectionTitle("Skenirani proizvodi (${scannedProducts.size})")
            }

            //items(scannedProducts) { product ->
            //    ProductMetadataRow(product)
          //  }
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

@Composable
fun ProductMetadataRow(product: ProductMetadata) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Column {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("Barkod: ${product.barcode}", style = MaterialTheme.typography.bodySmall)
                Text("P: ${product.proteinPer100g}g | C: ${product.caloriesPer100g}kcal", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}