package com.example.zilean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.zilean.ui.ZileanApp
import com.example.zilean.ui.theme.ZileanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("ZileanPrefs", android.content.Context.MODE_PRIVATE)

        val prefKeys = listOf("goal_water", "goal_protein", "goal_calories", "goal_carbs", "goal_fats")
        prefKeys.forEach { key ->
            try {
                sharedPref.getInt(key, 0)
            } catch (e: ClassCastException) {
                sharedPref.edit().remove(key).apply()
            }
        }

        setContent {
            var isDarkMode by remember {
                mutableStateOf(sharedPref.getBoolean("is_dark_mode", false))
            }

            var isSetupDone by remember {
                mutableStateOf(sharedPref.getBoolean("is_setup_done", false))
            }
            var userName by remember {
                mutableStateOf(sharedPref.getString("user_name", "person") ?: "person")
            }
            var goalProt by remember {
                mutableStateOf(sharedPref.getInt("goal_protein", 190))
            }
            var goalCal by remember {
                mutableStateOf(sharedPref.getInt("goal_calories", 2000))
            }
            var goalCarb by remember {
                mutableStateOf(sharedPref.getInt("goal_carbs", 200))
            }
            var goalFat by remember {
                mutableStateOf(sharedPref.getInt("goal_fats", 70))
            }
            var goalWater by remember {
                mutableStateOf(sharedPref.getInt("goal_water", 1000))
            }

            ZileanTheme(darkTheme = isDarkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ZileanApp(
                        isSetupDone = isSetupDone,
                        isDarkMode = isDarkMode,
                        onThemeToggle = { dark ->
                            isDarkMode = dark
                            sharedPref.edit().putBoolean("is_dark_mode", dark).apply()
                        },
                        userName = userName,
                        goalProt = goalProt,
                        goalCal = goalCal,
                        goalCarb = goalCarb,
                        goalFat = goalFat,
                        goalWater = goalWater,
                        onSaveUserData = { name, cal, prot, carb, fat, water ->
                            saveUserData(name, cal, prot, carb, fat, water)
                            userName = name
                            goalCal = cal.toIntOrNull() ?: 0
                            goalProt = prot.toIntOrNull() ?: 0
                            goalCarb = carb.toIntOrNull() ?: 0
                            goalFat = fat.toIntOrNull() ?: 0
                            goalWater = water.toIntOrNull() ?: 0
                            isSetupDone = true
                        }
                    )
                }
            }
        }
    }

    private fun saveUserData(name: String, cal: String, prot: String, carb: String, fat: String, water: String) {
        val sharedPref = getSharedPreferences("ZileanPrefs", android.content.Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_name", name)
            putInt("goal_calories", cal.toIntOrNull() ?: 0)
            putInt("goal_protein", prot.toIntOrNull() ?: 0)
            putInt("goal_carbs", carb.toIntOrNull() ?: 0)
            putInt("goal_fats", fat.toIntOrNull() ?: 0)
            putInt("goal_water", water.toIntOrNull() ?: 0)
            putBoolean("is_setup_done", true)
            apply()
        }
    }



}