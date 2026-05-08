package com.example.zilean.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_metadata")
data class ProductMetadata(
    @PrimaryKey val barcode: String,
    val name: String,
    val proteinPer100g: Int,
    val caloriesPer100g: Int,
    val carbsPer100g: Int = 0,
    val fatsPer100g: Int = 0
)