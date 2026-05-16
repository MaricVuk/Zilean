package com.example.zilean.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zilean.data.FoodEntry
import com.example.zilean.data.MealPreset
import com.example.zilean.data.ProductMetadata
import com.example.zilean.data.getDatabaseBuilder
import com.example.zilean.data.getRoomDatabase
import com.example.zilean.getScanner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModel : ViewModel() {
    private val db = getRoomDatabase(getDatabaseBuilder())
    private val dao = db.foodDao()
    private val scanner = getScanner()

    private val _selectedDate = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun changeDate(days: Int) {
        _selectedDate.value = _selectedDate.value + DatePeriod(days = days)
    }

    val allFood: Flow<List<FoodEntry>> = dao.getAllFood()
    val allPresets: Flow<List<MealPreset>> = dao.getAllPresets()
    val allProducts: Flow<List<ProductMetadata>> = dao.getAllProducts()

    private fun <T> getTodaysData(query: (Long, Long) -> Flow<T>): Flow<T> = 
        _selectedDate.flatMapLatest { date ->
            val tz = TimeZone.currentSystemDefault()
            val startOfDay = date.atStartOfDayIn(tz).toEpochMilliseconds()
            val endOfDay = date.plus(DatePeriod(days = 1)).atStartOfDayIn(tz).toEpochMilliseconds()
            query(startOfDay, endOfDay)
        }

    fun getTodaysProtein(): Flow<Int?> = getTodaysData { s, e -> dao.getProteinForDate(s, e) }
    fun getTodaysCalories(): Flow<Int?> = getTodaysData { s, e -> dao.getCaloriesForDate(s, e) }
    fun getTodaysCarbs(): Flow<Int?> = getTodaysData { s, e -> dao.getCarbsForDate(s, e) }
    fun getTodaysFats(): Flow<Int?> = getTodaysData { s, e -> dao.getFatsForDate(s, e) }
    fun getTodaysWater(): Flow<Int?> = getTodaysData { s, e -> dao.getWaterForDate(s, e) }
    fun getTodaysFoodEntries(): Flow<List<FoodEntry>> = getTodaysData { s, e -> dao.getFoodListForDate(s, e) }
    fun getTodaysWaterEntries(): Flow<List<FoodEntry>> = getTodaysData { s, e -> dao.getWaterListForDate(s, e) }

    fun addFood(name: String, protein: Int, calories: Int, carbs: Int, fats: Int) {
        viewModelScope.launch {
            val tz = TimeZone.currentSystemDefault()
            val selectedDate = _selectedDate.value
            val now = Clock.System.now()
            val today = now.toLocalDateTime(tz).date
            
            val timestamp = if (selectedDate == today) {
                now.toEpochMilliseconds()
            } else {
                selectedDate.atTime(12, 0).toInstant(tz).toEpochMilliseconds()
            }
            
            val newEntry = FoodEntry(
                name = name,
                protein = protein,
                calories = calories,
                carbs = carbs,
                fats = fats,
                date = timestamp
            )
            dao.insertFood(newEntry)
        }
    }

    fun addWater(amount: Int) {
        viewModelScope.launch {
            val tz = TimeZone.currentSystemDefault()
            val selectedDate = _selectedDate.value
            val now = Clock.System.now()
            val today = now.toLocalDateTime(tz).date

            val timestamp = if (selectedDate == today) {
                now.toEpochMilliseconds()
            } else {
                selectedDate.atTime(12, 0).toInstant(tz).toEpochMilliseconds()
            }

            val newEntry = FoodEntry(
                name = "Voda",
                calories = 0,
                protein = 0,
                carbs = 0,
                fats = 0,
                water = amount,
                isWater = true,
                date = timestamp
            )
            dao.insertFood(newEntry)
        }
    }

    fun deleteEntry(entry: FoodEntry) {
        viewModelScope.launch { dao.deleteFood(entry.id) }
    }

    fun scanBarcode(onProductFound: (ProductMetadata?, String) -> Unit) {
        scanner.scanBarcode(onProductFound)
    }

    fun saveProductMetadata(product: ProductMetadata) {
        viewModelScope.launch { dao.insertProductMetadata(product) }
    }

    fun addPreset(name: String, protein: Int, calories: Int, carbs: Int, fats: Int) {
        viewModelScope.launch { 
            dao.insertPreset(MealPreset(name = name, protein = protein, calories = calories, carbs = carbs, fats = fats)) 
        }
    }

    fun deletePreset(preset: MealPreset) {
        viewModelScope.launch { dao.deletePreset(preset) }
    }

    fun editPreset(preset: MealPreset) {
        viewModelScope.launch { dao.updatePreset(preset) }
    }

    fun editFoodEntry(entry: FoodEntry) {
        viewModelScope.launch { dao.updateFoodEntry(entry) }
    }

    fun deleteProduct(product: ProductMetadata) {
        viewModelScope.launch { dao.deleteProduct(product) }
    }

    fun editProduct(product: ProductMetadata) {
        viewModelScope.launch { dao.updateProductMetadata(product) }
    }

    suspend fun exportData(): String {
        val allEntries = dao.getAllFoodList()
        val allPresets = dao.getAllPresetsList()
        val allProducts = dao.getAllProductsList()
        
        val sb = StringBuilder()
        
        sb.append("BEGIN_FOOD_ENTRIES\n")
        sb.append("name;calories;protein;carbs;fats;water;isWater;date\n")
        allEntries.forEach { e ->
            sb.append("${e.name};${e.calories};${e.protein};${e.carbs};${e.fats};${e.water};${if (e.isWater) 1 else 0};${e.date}\n")
        }
        sb.append("END_FOOD_ENTRIES\n")
        
        sb.append("BEGIN_MEAL_PRESETS\n")
        sb.append("name;calories;protein;carbs;fats\n")
        allPresets.forEach { p ->
            sb.append("${p.name};${p.calories};${p.protein};${p.carbs};${p.fats}\n")
        }
        sb.append("END_MEAL_PRESETS\n")
        
        sb.append("BEGIN_PRODUCT_METADATA\n")
        sb.append("barcode;name;caloriesPer100g;proteinPer100g;carbsPer100g;fatsPer100g\n")
        allProducts.forEach { p ->
            sb.append("${p.barcode};${p.name};${p.caloriesPer100g};${p.proteinPer100g};${p.carbsPer100g};${p.fatsPer100g}\n")
        }
        sb.append("END_PRODUCT_METADATA\n")
        
        return sb.toString()
    }

    fun importData(data: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val lines = data.lines().map { it.trim() }
                var currentSection = ""
                
                lines.forEach { line ->
                    if (line.isBlank()) return@forEach
                    
                    when (line) {
                        "BEGIN_FOOD_ENTRIES" -> { currentSection = "FOOD"; return@forEach }
                        "BEGIN_MEAL_PRESETS" -> { currentSection = "PRESETS"; return@forEach }
                        "BEGIN_PRODUCT_METADATA" -> { currentSection = "PRODUCTS"; return@forEach }
                        "END_FOOD_ENTRIES", "END_MEAL_PRESETS", "END_PRODUCT_METADATA" -> { currentSection = ""; return@forEach }
                    }
                    
                    if (currentSection == "") return@forEach
                    
                    if (line.startsWith("name;calories") || line.startsWith("barcode;name")) return@forEach
                    
                    val p = line.split(";")
                    when (currentSection) {
                        "FOOD" -> {
                            if (p.size >= 8) {
                                dao.insertFood(FoodEntry(
                                    name = p[0],
                                    calories = p[1].toIntOrNull() ?: 0,
                                    protein = p[2].toIntOrNull() ?: 0,
                                    carbs = p[3].toIntOrNull() ?: 0,
                                    fats = p[4].toIntOrNull() ?: 0,
                                    water = p[5].toIntOrNull() ?: 0,
                                    isWater = p[6] == "1",
                                    date = p[7].toLongOrNull() ?: Clock.System.now().toEpochMilliseconds()
                                ))
                            }
                        }
                        "PRESETS" -> {
                            if (p.size >= 5) {
                                dao.insertPreset(MealPreset(
                                    name = p[0],
                                    calories = p[1].toIntOrNull() ?: 0,
                                    protein = p[2].toIntOrNull() ?: 0,
                                    carbs = p[3].toIntOrNull() ?: 0,
                                    fats = p[4].toIntOrNull() ?: 0
                                ))
                            }
                        }
                        "PRODUCTS" -> {
                            if (p.size >= 6) {
                                dao.insertProductMetadata(ProductMetadata(
                                    barcode = p[0],
                                    name = p[1],
                                    caloriesPer100g = p[2].toIntOrNull() ?: 0,
                                    proteinPer100g = p[3].toIntOrNull() ?: 0,
                                    carbsPer100g = p[4].toIntOrNull() ?: 0,
                                    fatsPer100g = p[5].toIntOrNull() ?: 0
                                ))
                            }
                        }
                    }
                }
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
}
