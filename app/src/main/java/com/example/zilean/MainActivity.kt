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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

                                val goalProt = sharedPref.getInt("goal_protein", 190)
                                val goalCal = sharedPref.getInt("goal_calories", 2000)
                                val goalCarb = sharedPref.getInt("goal_carbs", 200)
                                val goalFat = sharedPref.getInt("goal_fats", 70)

                                HealthDashboard(
                                    proteinGoal = goalProt,
                                    caloriesGoal = goalCal,
                                    carbsGoal = goalCarb,
                                    fatsGoal = goalFat
                                )

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