package com.example.zilean.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zilean.data.ProductMetadata
import com.example.zilean.ui.*

@Composable
fun ZileanApp(
    isSetupDone: Boolean,
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    userName: String,
    goalProt: Int,
    goalCal: Int,
    goalCarb: Int,
    goalFat: Int,
    onSaveUserData: (String, String, String, String, String) -> Unit
) {
    var currentScreen by remember {
        mutableStateOf(if (isSetupDone) "dashboard" else "setup")
    }

    Scaffold { innerPadding ->
        val viewModel: FoodViewModel = viewModel()
        val todaysProtein by viewModel.getTodaysProtein().collectAsState(initial = 0)
        val todaysCalories by viewModel.getTodaysCalories().collectAsState(initial = 0)
        val todaysCarbs by viewModel.getTodaysCarbs().collectAsState(initial = 0)
        val todaysFats by viewModel.getTodaysFats().collectAsState(initial = 0)
        val foodList by viewModel.getTodaysFoodEntries().collectAsState(initial = emptyList())
        val selectedDate by viewModel.selectedDate.collectAsState()

        var showDialog by remember { mutableStateOf(false) }
        var scannedBarcode by remember { mutableStateOf<String?>(null) }
        var initialData by remember { mutableStateOf<ProductMetadata?>(null) }

        val context = LocalContext.current

        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "setup" -> {
                    SetupScreen(onSetupComplete = { name, cal, prot, carb, fat ->
                        onSaveUserData(name, cal, prot, carb, fat)
                        currentScreen = "dashboard"
                    })
                }

                "jelovnik" -> {
                    val presets by viewModel.allPresets.collectAsState(initial = emptyList())
                    MealMenuScreen(
                        presets = presets,
                        onBack = { currentScreen = "dashboard" },
                        onDeletePreset = { preset -> viewModel.deletePreset(preset) },
                        onPresetSelected = { preset ->
                            viewModel.addFood(preset.name, preset.protein, preset.calories, preset.carbs, preset.fats)
                            currentScreen = "dashboard"
                        },
                        viewModel = viewModel
                    )
                }

                "settings" -> {
                    SettingsScreen(
                        name = userName,
                        onBack = { currentScreen = "dashboard" },
                        isDarkMode = isDarkMode,
                        onThemeToggle = onThemeToggle,
                    )
                }

                else -> {
                    if (showDialog) {
                        key(scannedBarcode, initialData) {
                            AddFoodDialog(
                                initialName = initialData?.name ?: "",
                                initialProtein = initialData?.proteinPer100g?.toString() ?: "",
                                initialCalories = initialData?.caloriesPer100g?.toString() ?: "",
                                initialCarbs = initialData?.carbsPer100g?.toString() ?: "",
                                initialFats = initialData?.fatsPer100g?.toString() ?: "",
                                onDismiss = {
                                    showDialog = false
                                    initialData = null
                                    scannedBarcode = null
                                },
                                onConfirm = { name, prot, cal, carb, fat, shouldSavePreset ->
                                    viewModel.addFood(name, prot, cal, carb, fat)
                                    if (shouldSavePreset) {
                                        viewModel.addPreset(name, prot, cal, carb, fat)
                                    }
                                    scannedBarcode?.let { code ->
                                        viewModel.saveProductMetadata(
                                            ProductMetadata(
                                                barcode = code,
                                                name = name,
                                                proteinPer100g = prot,
                                                caloriesPer100g = cal,
                                                carbsPer100g = carb,
                                                fatsPer100g = fat
                                            )
                                        )
                                    }
                                    showDialog = false
                                    scannedBarcode = null
                                    initialData = null
                                }
                            )
                        }
                    }

                    HealthDashboard(
                        name = userName,
                        currentCalories = todaysCalories ?: 0,
                        currentProtein = todaysProtein ?: 0,
                        currentCarb = todaysCarbs ?: 0,
                        currentFat = todaysFats ?: 0,
                        proteinGoal = goalProt,
                        caloriesGoal = goalCal,
                        carbsGoal = goalCarb,
                        fatsGoal = goalFat,
                        foodList = foodList,
                        onAddFoodClick = { showDialog = true },
                        onDeleteEntry = { entry -> viewModel.deleteEntry(entry) },
                        onScanClick = {
                            viewModel.scanBarcode(context) { metadata, barcode ->
                                if (metadata != null) {
                                    viewModel.addFood(
                                        name = metadata.name,
                                        protein = metadata.proteinPer100g,
                                        calories = metadata.caloriesPer100g,
                                        carbs = metadata.carbsPer100g,
                                        fats = metadata.fatsPer100g
                                    )
                                } else {
                                    scannedBarcode = barcode
                                    initialData = null
                                    showDialog = true
                                }
                            }
                        },
                        onMenuClick = { currentScreen = "jelovnik" },
                        onEditEntry = { entry -> viewModel.editFoodEntry(entry) },
                        onSettingsClick = { currentScreen = "settings" },
                        selectedDate = selectedDate,
                        onDateChange = { date -> viewModel.setSelectedDate(date) },
                        onPreviousDay = { viewModel.changeDate(-1) },
                        onNextDay = { viewModel.changeDate(1) }
                    )
                }
            }
        }
    }
}
