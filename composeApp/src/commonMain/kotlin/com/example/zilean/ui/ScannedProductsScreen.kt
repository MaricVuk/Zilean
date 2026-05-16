package com.example.zilean.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zilean.data.ProductMetadata

@Composable
fun ScannedProductsScreen(
    products: List<ProductMetadata>,
    onBack: () -> Unit,
    onDeleteProduct: (ProductMetadata) -> Unit,
    viewModel: FoodViewModel
) {
    var editingProduct by remember { mutableStateOf<ProductMetadata?>(null) }

    editingProduct?.let { product ->
        AddFoodDialog(
            initialName = product.name,
            initialProtein = product.proteinPer100g.toString(),
            initialCalories = product.caloriesPer100g.toString(),
            initialCarbs = product.carbsPer100g.toString(),
            initialFats = product.fatsPer100g.toString(),
            showSavePresetOption = false,
            onDismiss = { editingProduct = null },
            onConfirm = { name, prot, cal, carb, fat, _ ->
                viewModel.editProduct(product.copy(
                    name = name,
                    proteinPer100g = prot,
                    caloriesPer100g = cal,
                    carbsPer100g = carb,
                    fatsPer100g = fat
                ))
                editingProduct = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Nazad")
            }
            Text("Skenirani proizvodi", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Nema skeniranih proizvoda.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(products) { product ->
                        ProductRow(
                            product = product,
                            onEdit = { editingProduct = product },
                            onDelete = { onDeleteProduct(product) }
                        )
                    }
                }
            }
    }
}

@Composable
fun ProductRow(
    product: ProductMetadata,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(
                    text = "${product.caloriesPer100g} kcal | P: ${product.proteinPer100g}g | C: ${product.carbsPer100g}g | F: ${product.fatsPer100g}g",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Barcode: ${product.barcode}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Column(
                modifier = Modifier.padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Izmeni",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Obrisi",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
