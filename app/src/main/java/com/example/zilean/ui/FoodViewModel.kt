package com.example.zilean.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zilean.data.AppDatabase
import com.example.zilean.data.FoodEntry
import com.example.zilean.data.MealPreset
import com.example.zilean.data.ProductMetadata
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).foodDao()

    private val _selectedDate = MutableStateFlow(Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    })
    val selectedDate: StateFlow<Calendar> = _selectedDate

    fun setSelectedDate(calendar: Calendar) {
        val newDate = (calendar.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        _selectedDate.value = newDate
    }

    fun changeDate(days: Int) {
        val newDate = (_selectedDate.value.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, days)
        }
        _selectedDate.value = newDate
    }


    val allFood: Flow<List<FoodEntry>> = dao.getAllFood()
    val allPresets: Flow<List<MealPreset>> = dao.getAllPresets()
    val allProducts: Flow<List<ProductMetadata>> = dao.getAllProducts()

    fun addFood(name: String, protein: Int, calories: Int, carbs: Int, fats: Int) {
        viewModelScope.launch {
            val newEntry = FoodEntry(
                name = name,
                protein = protein,
                calories = calories,
                carbs = carbs,
                fats = fats,
                date = _selectedDate.value.timeInMillis + (System.currentTimeMillis() % (24 * 60 * 60 * 1000))
            )
            dao.insertFood(newEntry)
        }
    }

    fun getTodaysProtein(): Flow<Int?> = _selectedDate.flatMapLatest { date ->
        val startOfDay = date.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000
        dao.getProteinForDate(startOfDay, endOfDay)
    }

    fun getTodaysCalories(): Flow<Int?> = _selectedDate.flatMapLatest { date ->
        val startOfDay = date.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000
        dao.getCaloriesForDate(startOfDay, endOfDay)
    }

    fun getTodaysCarbs(): Flow<Int?> = _selectedDate.flatMapLatest { date ->
        val startOfDay = date.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000
        dao.getCarbsForDate(startOfDay, endOfDay)
    }

    fun getTodaysFats(): Flow<Int?> = _selectedDate.flatMapLatest { date ->
        val startOfDay = date.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000
        dao.getFatsForDate(startOfDay, endOfDay)
    }

    fun getTodaysWater(): Flow<Int?> = _selectedDate.flatMapLatest { date ->
        val startOfDay = date.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000
        dao.getWaterForDate(startOfDay, endOfDay)
    }

    fun getTodaysFoodEntries(): Flow<List<FoodEntry>> = _selectedDate.flatMapLatest { date ->
        val startOfDay = date.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000
        dao.getFoodListForDate(startOfDay, endOfDay)
    }

    fun addWater(amount: Int) {
        viewModelScope.launch {
            val newEntry = FoodEntry(
                name = "Voda",
                calories = 0,
                protein = 0,
                carbs = 0,
                fats = 0,
                water = amount,
                isWater = true,
                date = _selectedDate.value.timeInMillis + (System.currentTimeMillis() % (24 * 60 * 60 * 1000))
            )
            dao.insertFood(newEntry)
        }
    }

    fun deleteEntry(entry: FoodEntry) {
        viewModelScope.launch {
            dao.deleteFood(entry.id)
        }
    }

    fun scanBarcode(context: Context, onProductFound: (ProductMetadata?, String) -> Unit) {
        val scanner = GmsBarcodeScanning.getClient(context)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val code = barcode.rawValue ?: return@addOnSuccessListener
                viewModelScope.launch {
                    val metadata = dao.getProductByBarcode(code)
                    onProductFound(metadata, code)
                }
            }
    }

    fun saveProductMetadata(product: ProductMetadata) {
        viewModelScope.launch {
            dao.insertProductMetadata(product)
        }
    }

    fun addPreset(name: String, protein: Int, calories: Int, carbs: Int, fats: Int) {
        viewModelScope.launch {
            dao.insertPreset(MealPreset(name = name, protein = protein, calories = calories, carbs = carbs, fats = fats))
        }
    }

    fun deletePreset(preset: MealPreset) {
        viewModelScope.launch {
            dao.deletePreset(preset)
        }
    }

    fun editPreset(preset: MealPreset) {
        viewModelScope.launch {
            dao.updatePreset(preset)
        }
    }

    fun editFoodEntry(entry: FoodEntry) {
        viewModelScope.launch {
            dao.updateFoodEntry(entry)
        }
    }

    fun deleteProduct(product: ProductMetadata) {
        viewModelScope.launch {
            dao.deleteProduct(product)
        }
    }

    fun editProduct(product: ProductMetadata) {
        viewModelScope.launch {
            dao.updateProductMetadata(product)
        }
    }



}