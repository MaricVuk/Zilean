package com.example.zilean.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zilean.data.AppDatabase
import com.example.zilean.data.FoodEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).foodDao()


    val allFood: Flow<List<FoodEntry>> = dao.getAllFood()

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


}