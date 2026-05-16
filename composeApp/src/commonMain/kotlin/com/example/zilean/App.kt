package com.example.zilean

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.zilean.ui.ZileanApp
import com.example.zilean.ui.theme.ZileanTheme
import com.russhwolf.settings.Settings

@Composable
fun App() {
    val settings = remember { ZileanSettings(Settings()) }
    
    var isDarkMode by remember { mutableStateOf(settings.isDarkMode) }
    var isSetupDone by remember { mutableStateOf(settings.isSetupDone) }
    var userName by remember { mutableStateOf(settings.userName) }
    var goalProt by remember { mutableStateOf(settings.goalProtein) }
    var goalCal by remember { mutableStateOf(settings.goalCalories) }
    var goalCarb by remember { mutableStateOf(settings.goalCarbs) }
    var goalFat by remember { mutableStateOf(settings.goalFats) }
    var goalWater by remember { mutableStateOf(settings.goalWater) }

    ZileanTheme(darkTheme = isDarkMode) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ZileanApp(
                isSetupDone = isSetupDone,
                isDarkMode = isDarkMode,
                onThemeToggle = { dark ->
                    isDarkMode = dark
                    settings.isDarkMode = dark
                },
                userName = userName,
                goalProt = goalProt,
                goalCal = goalCal,
                goalCarb = goalCarb,
                goalFat = goalFat,
                goalWater = goalWater,
                onSaveUserData = { name, cal, prot, carb, fat, water ->
                    val c = cal.toIntOrNull() ?: 0
                    val p = prot.toIntOrNull() ?: 0
                    val cb = carb.toIntOrNull() ?: 0
                    val f = fat.toIntOrNull() ?: 0
                    val w = water.toIntOrNull() ?: 0
                    
                    settings.userName = name
                    settings.goalCalories = c
                    settings.goalProtein = p
                    settings.goalCarbs = cb
                    settings.goalFats = f
                    settings.goalWater = w
                    settings.isSetupDone = true
                    
                    userName = name
                    goalCal = c
                    goalProt = p
                    goalCarb = cb
                    goalFat = f
                    goalWater = w
                    isSetupDone = true
                }
            )
        }
    }
}
