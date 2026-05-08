package com.example.zilean.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zilean.data.FoodEntry
import com.example.zilean.ui.theme.ZileanTheme

@Composable
fun HealthDashboard(
    name: String?,
    currentCalories: Int,
    currentProtein: Int,
    currentCarb: Int,
    currentFat: Int,
    proteinGoal: Int,
    caloriesGoal: Int,
    carbsGoal: Int,
    fatsGoal: Int,
    foodList: List<FoodEntry>,
    onAddFoodClick: () -> Unit,
    onDeleteEntry: (FoodEntry) -> Unit,
    onScanClick: () -> Unit,
    onMenuClick: () -> Unit,
    onEditEntry: (FoodEntry) -> Unit,
    onSettingsClick: () -> Unit
) {
    val progress = if (caloriesGoal > 0) (currentCalories * 100) / caloriesGoal else 0
    var editingEntry by remember { mutableStateOf<FoodEntry?>(null) }

    editingEntry?.let { entry ->
        AddFoodDialog(
            initialName = entry.name,
            initialProtein = entry.protein.toString(),
            initialCalories = entry.calories.toString(),
            initialCarbs = entry.carbs.toString(),
            initialFats = entry.fats.toString(),
            showSavePresetOption = false,
            onDismiss = { editingEntry = null },
            onConfirm = { name, prot, cal, carb, fat, _ ->
                onEditEntry(entry.copy(
                    name = name, protein = prot, calories = cal, carbs = carb, fats = fat
                ))
                editingEntry = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        //progress = 0.65f,
                        progress = progress.toFloat() / 100,
                        modifier = Modifier.size(100.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 8.dp,
                        trackColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        progress.toString() + "%",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                Column {
                    Text("Kalorije", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 18.sp)
                    Row (verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)){
                        Text(
                            currentCalories.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "/"  + caloriesGoal.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal

                        )
                    }
                    Text("kcal", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MacroCard(
                label = "Hidrati",
                value = currentCarb.toString(),
                value2 = carbsGoal.toString(),
                color = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
            MacroCard(
                label = "Proteini",
                value = currentProtein.toString(),
                value2 = proteinGoal.toString(),
                color = Color(0xFFc40404),
                modifier = Modifier.weight(1f)
            )
            MacroCard(
                label = "Masti",
                value = currentFat.toString(),
                value2 = fatsGoal.toString(),
                color = Color(0xFFFACC15),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))



        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onAddFoodClick,
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(64.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dodaj obrok",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.width(1.dp))

            FilledIconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(64.dp).fillMaxWidth(0.2f),
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(44.dp),
                    contentDescription = "Otvori jelovnik"
                )
            }

            Spacer(modifier = Modifier.width(1.dp))

            FilledIconButton(
                onClick = onScanClick,
                modifier = Modifier.size(64.dp).fillMaxWidth(0.2f),
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Skeniraj bar-kod",
                    Modifier.size(44.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(1.dp))

            FilledIconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(64.dp).fillMaxWidth(0.2f),
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    Modifier.size(44.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

        }



        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Danasnji obroci:", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)

        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp),
            ) {
            items(foodList) { currentEntry ->
                FoodItemRow(
                    entry = currentEntry,
                    onDelete = { onDeleteEntry(currentEntry) },
                    onEdit = { editingEntry = currentEntry },
                )
            }
        }
    }
}

@Composable
fun MacroCard(
    label: String,
    value: String,
    value2: String,
    color: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row (
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()

                )
                Text(
                    text = "/" + value2,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.alignByBaseline()

                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun DashboardLightPreview() {
    ZileanTheme(darkTheme = false) {
        HealthDashboard(
            name = "Vuk",
            currentCalories = 1538,
            currentProtein = 73,
            currentCarb = 127,
            currentFat = 13,

            proteinGoal = 250,
            caloriesGoal = 2750,
            carbsGoal = 600,
            fatsGoal = 80,
            foodList = emptyList(),
            onAddFoodClick = { true },
            onDeleteEntry = {true },
            onScanClick = {true},
            onMenuClick = {true},
            onEditEntry = {true},
            onSettingsClick = {true},

        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardDarkPreview() {
    ZileanTheme(darkTheme = true) {
        HealthDashboard(
            name = "Vuk",
            currentCalories = 1538,
            currentProtein = 73,
            currentCarb = 127,
            currentFat = 13,

            proteinGoal = 250,
            caloriesGoal = 2750,
            carbsGoal = 600,
            fatsGoal = 80,
            foodList = emptyList(),
            onAddFoodClick = { true },
            onDeleteEntry = {true },
            onScanClick = {true},
            onMenuClick = {true},
            onEditEntry = {true},
            onSettingsClick = {true},

        )
    }
}