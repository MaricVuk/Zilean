package com.example.zilean.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock

@Entity(tableName = "food_entries")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val water: Int = 0,
    val isWater: Boolean = false,
    val date: Long = Clock.System.now().toEpochMilliseconds()
)






