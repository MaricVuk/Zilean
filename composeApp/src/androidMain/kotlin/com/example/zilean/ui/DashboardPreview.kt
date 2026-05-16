package com.example.zilean.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.zilean.data.FoodEntry
import com.example.zilean.ui.theme.ZileanTheme
import kotlinx.datetime.LocalDate

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun HealthDashboardPreview() {
    ZileanTheme(darkTheme = false) {
        DashboardContent()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun HealthDashboardDarkPreview() {
    ZileanTheme(darkTheme = true) {
        DashboardContent()
    }
}

@Composable
private fun DashboardContent() {
    HealthDashboard(
        name = "Korisnik",
        currentCalories = 1450,
        currentProtein = 95,
        currentCarb = 180,
        currentFat = 55,
        proteinGoal = 150,
        caloriesGoal = 2000,
        carbsGoal = 250,
        fatsGoal = 70,
        foodList = listOf(
            FoodEntry(id = 1, name = "Piletina sa pirinčem", calories = 650, protein = 45, carbs = 55, fats = 12),
            FoodEntry(id = 2, name = "Grčki jogurt", calories = 150, protein = 15, carbs = 6, fats = 4),
            FoodEntry(id = 3, name = "Bademi", calories = 200, protein = 7, carbs = 5, fats = 18)
        ),
        onAddFoodClick = {},
        onDeleteEntry = {},
        onScanClick = {},
        onMenuClick = {},
        onEditEntry = {},
        onSettingsClick = {},
        selectedDate = LocalDate(2024, 5, 20),
        onDateChange = {},
        onPreviousDay = {},
        onNextDay = {},
        waterGoal = 2500,
        currentWater = 1500,
        onAddWater = {},
        onWaterCardClick = {}
    )
}
