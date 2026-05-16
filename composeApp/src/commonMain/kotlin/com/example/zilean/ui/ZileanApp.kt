package com.example.zilean.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zilean.data.ProductMetadata

enum class Screen {
    Setup, Dashboard, Jelovnik, Settings, ScannedProducts, Goals, WaterIntake
}

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
    onSaveUserData: (String, String, String, String, String, String) -> Unit,
    goalWater: Int,
) {
    var currentScreen by remember {
        mutableStateOf(if (isSetupDone) Screen.Dashboard else Screen.Setup)
    }

    val viewModel: FoodViewModel = viewModel { FoodViewModel() }

    Scaffold { innerPadding ->
        val todaysProtein by viewModel.getTodaysProtein().collectAsState(initial = 0)
        val todaysCalories by viewModel.getTodaysCalories().collectAsState(initial = 0)
        val todaysCarbs by viewModel.getTodaysCarbs().collectAsState(initial = 0)
        val todaysFats by viewModel.getTodaysFats().collectAsState(initial = 0)
        val todaysWater by viewModel.getTodaysWater().collectAsState(initial = 0)
        val foodList by viewModel.getTodaysFoodEntries().collectAsState(initial = emptyList())
        val waterEntries by viewModel.getTodaysWaterEntries().collectAsState(initial = emptyList())
        val selectedDate by viewModel.selectedDate.collectAsState()

        var showDialog by remember { mutableStateOf(false) }
        var scannedBarcode by remember { mutableStateOf<String?>(null) }
        var initialData by remember { mutableStateOf<ProductMetadata?>(null) }

        Box(modifier = Modifier.padding(innerPadding)) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    val forward = slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                    val backward = slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()

                    when {
                        targetState == Screen.Dashboard -> backward
                        initialState == Screen.Dashboard || initialState == Screen.Setup -> forward
                        targetState == Screen.Settings -> backward
                        else -> forward
                    }.using(SizeTransform(clip = false))
                },
                modifier = Modifier.fillMaxSize(),
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.Setup -> {
                        SetupScreen(onSetupComplete = { name, cal, prot, carb, fat, water ->
                            onSaveUserData(name, cal, prot, carb, fat, water)
                            currentScreen = Screen.Dashboard
                        })
                    }

                    Screen.Jelovnik -> {
                        val presets by viewModel.allPresets.collectAsState(initial = emptyList())
                        MealMenuScreen(
                            presets = presets,
                            onBack = { currentScreen = Screen.Dashboard },
                            onDeletePreset = { preset -> viewModel.deletePreset(preset) },
                            onPresetSelected = { preset ->
                                viewModel.addFood(preset.name, preset.protein, preset.calories, preset.carbs, preset.fats)
                                currentScreen = Screen.Dashboard
                            },
                            viewModel = viewModel
                        )
                    }

                    Screen.Settings -> {
                        SettingsScreen(
                            name = userName,
                            onBack = { currentScreen = Screen.Dashboard },
                            isDarkMode = isDarkMode,
                            onThemeToggle = onThemeToggle,
                            onOpenGoals = { currentScreen = Screen.Goals },
                            onOpenScannedProducts = { currentScreen = Screen.ScannedProducts },
                            onUpdateName = { newName ->
                                onSaveUserData(newName, goalCal.toString(), goalProt.toString(), goalCarb.toString(), goalFat.toString(), goalWater.toString())
                            },
                            onExportData = { viewModel.exportData() },
                            onImportData = { data, onComplete -> viewModel.importData(data, onComplete) }
                        )
                    }

                    Screen.ScannedProducts -> {
                        val products by viewModel.allProducts.collectAsState(initial = emptyList())
                        ScannedProductsScreen(
                            products = products,
                            onBack = { currentScreen = Screen.Settings },
                            onDeleteProduct = { product -> viewModel.deleteProduct(product) },
                            viewModel = viewModel
                        )
                    }

                    Screen.Goals -> {
                        GoalsScreen(
                            currentCalories = goalCal,
                            currentProtein = goalProt,
                            currentCarbs = goalCarb,
                            currentFats = goalFat,
                            currentWater = goalWater,
                            onSaveGoals = { cal, prot, carb, fat, water ->
                                onSaveUserData(userName, cal.toString(), prot.toString(), carb.toString(), fat.toString(), water.toString())
                            },
                            onBack = { currentScreen = Screen.Settings }
                        )
                    }

                    Screen.WaterIntake -> {
                        WaterIntakeScreen(
                            waterEntries = waterEntries,
                            onBack = { currentScreen = Screen.Dashboard },
                            onDeleteEntry = { entry -> viewModel.deleteEntry(entry) },
                            onEditEntry = { entry -> viewModel.editFoodEntry(entry) }
                        )
                    }

                    Screen.Dashboard -> {
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
                            waterGoal = goalWater,
                            currentWater = todaysWater ?: 0,
                            onAddWater = { amount -> viewModel.addWater(amount) },
                            foodList = foodList,
                            onAddFoodClick = { showDialog = true },
                            onDeleteEntry = { entry -> viewModel.deleteEntry(entry) },
                            onScanClick = {
                                viewModel.scanBarcode { metadata, barcode ->
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
                            onMenuClick = { currentScreen = Screen.Jelovnik },
                            onEditEntry = { entry -> viewModel.editFoodEntry(entry) },
                            onSettingsClick = { currentScreen = Screen.Settings },
                            selectedDate = selectedDate,
                            onDateChange = { date -> viewModel.setSelectedDate(date) },
                            onPreviousDay = { viewModel.changeDate(-1) },
                            onNextDay = { viewModel.changeDate(1) },
                            onWaterCardClick = { currentScreen = Screen.WaterIntake }
                        )
                    }
                }
            }
        }
    }
}
