package com.example.zilean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zilean.ui.AddFoodDialog
import com.example.zilean.ui.FoodViewModel
import com.example.zilean.ui.HealthDashboard
import com.example.zilean.ui.SetupScreen

import com.example.zilean.ui.theme.ZileanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("ZileanPrefs", android.content.Context.MODE_PRIVATE)
        val isSetupDone = sharedPref.getBoolean("is_setup_done", false)

        setContent {
            ZileanTheme {
                var currentScreen by remember {
                    mutableStateOf(if (isSetupDone) "dashboard" else "setup")
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            if (currentScreen == "setup") {
                                SetupScreen(onSetupComplete = { name, cal, prot, carb, fat ->
                                    saveUserData(name, cal, prot, carb, fat)
                                    currentScreen = "dashboard"
                                })
                            } else {

                                val userName = sharedPref.getString("user_name", "person")
                                val goalProt = sharedPref.getInt("goal_protein", 190)
                                val goalCal = sharedPref.getInt("goal_calories", 2000)
                                val goalCarb = sharedPref.getInt("goal_carbs", 200)
                                val goalFat = sharedPref.getInt("goal_fats", 70)

                                val viewModel: FoodViewModel = viewModel()
                                val todaysProtein by viewModel.getTodaysProtein().collectAsState(initial = 0)
                                val todaysCalories by viewModel.getTodaysCalories().collectAsState(initial = 0)
                                val todaysCarbs by viewModel.getTodaysCarbs().collectAsState(initial = 0)
                                val todaysFats by viewModel.getTodaysFats().collectAsState(initial = 0)


                                var showDialog by remember { mutableStateOf(false) }

                                if (showDialog) {
                                    AddFoodDialog(
                                        onDismiss = { showDialog = false },
                                        onConfirm = { name, prot, cal, carb, fat ->
                                            viewModel.addFood(name, prot, cal, carb, fat)
                                            showDialog = false
                                        }
                                    )
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
                                    onAddFoodClick = { showDialog = true } // Sada dugme samo otvara dijalog!
                                )


                                /*HealthDashboard(
                                    proteinGoal = goalProt,
                                    caloriesGoal = goalCal,
                                    carbsGoal = goalCarb,
                                    fatsGoal = goalFat
                                )*/

                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveUserData(name: String, cal: String, prot: String, carb: String, fat: String) {
        val sharedPref = getSharedPreferences("ZileanPrefs", android.content.Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_name", name)
            putInt("goal_calories", cal.toIntOrNull() ?: 0)
            putInt("goal_protein", prot.toIntOrNull() ?: 0)
            putInt("goal_carbs", carb.toIntOrNull() ?: 0)
            putInt("goal_fats", fat.toIntOrNull() ?: 0)
            putBoolean("is_setup_done", true)
            apply()
        }
    }



}