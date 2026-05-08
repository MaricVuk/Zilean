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

        setContent {
            var isDarkMode by remember {
                mutableStateOf(sharedPref.getBoolean("is_dark_mode", false))
            }

            ZileanTheme(darkTheme = isDarkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ZileanApp(
                        isSetupDone = sharedPref.getBoolean("is_setup_done", false),
                        isDarkMode = isDarkMode,
                        onThemeToggle = { dark ->
                            isDarkMode = dark
                            sharedPref.edit().putBoolean("is_dark_mode", dark).apply()
                        },
                        userName = sharedPref.getString("user_name", "person") ?: "person",
                        goalProt = sharedPref.getInt("goal_protein", 190),
                        goalCal = sharedPref.getInt("goal_calories", 2000),
                        goalCarb = sharedPref.getInt("goal_carbs", 200),
                        goalFat = sharedPref.getInt("goal_fats", 70),
                        onSaveUserData = { name, cal, prot, carb, fat ->
                            saveUserData(name, cal, prot, carb, fat)
                        }
                    )
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