package com.example.zilean.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_presets")
data class MealPreset(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val protein: Int,
    val calories: Int,
    val carbs: Int,
    val fats: Int
)