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
import kotlinx.coroutines.launch
import java.util.*

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).foodDao()


    val allFood: Flow<List<FoodEntry>> = dao.getAllFood()
    val allPresets: Flow<List<MealPreset>> = dao.getAllPresets()

    fun addFood(name: String, protein: Int, calories: Int, carbs: Int, fats: Int) {
        viewModelScope.launch {
            val newEntry = FoodEntry(
                name = name,
                protein = protein,
                calories = calories,
                carbs = carbs,
                fats = fats
            )
            dao.insertFood(newEntry)
        }
    }

    fun getTodaysProtein(): Flow<Int?> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getTodaysProtein(calendar.timeInMillis)
    }
    fun getTodaysCalories(): Flow<Int?> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getTodaysCalories(calendar.timeInMillis)
    }
    fun getTodaysCarbs(): Flow<Int?> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getTodaysCarbs(calendar.timeInMillis)
    }
    fun getTodaysFats(): Flow<Int?> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getTodaysFats(calendar.timeInMillis)
    }


    fun getTodaysFoodEntries(): Flow<List<FoodEntry>> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getTodaysFoodList(calendar.timeInMillis)
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


}