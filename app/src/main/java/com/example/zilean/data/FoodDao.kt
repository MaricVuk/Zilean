package com.example.zilean.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: FoodEntry)

    @Query("SELECT * FROM food_entries ORDER BY date DESC")
    fun getAllFood(): Flow<List<FoodEntry>>

    @Query("SELECT SUM(protein) FROM food_entries WHERE date >= :startOfDay AND date < :endOfDay")
    fun getProteinForDate(startOfDay: Long, endOfDay: Long): Flow<Int?>

    @Query ("SELECT SUM(calories) FROM food_entries WHERE date >= :startOfDay AND date < :endOfDay")
    fun getCaloriesForDate(startOfDay: Long, endOfDay: Long): Flow<Int?>

    @Query ("SELECT SUM(fats) FROM food_entries WHERE date >= :startOfDay AND date < :endOfDay")
    fun getFatsForDate(startOfDay: Long, endOfDay: Long): Flow<Int?>

    @Query("SELECT SUM(carbs) FROM food_entries WHERE date >= :startOfDay AND date < :endOfDay")
    fun getCarbsForDate(startOfDay: Long, endOfDay: Long): Flow<Int?>

    @Query("SELECT SUM(water) FROM food_entries WHERE date >= :startOfDay AND date < :endOfDay AND isWater = 1")
    fun getWaterForDate(startOfDay: Long, endOfDay: Long): Flow<Int?>

    @Query("SELECT * FROM food_entries WHERE date >= :startOfDay AND date < :endOfDay AND isWater = 0 ORDER BY date DESC")
    fun getFoodListForDate(startOfDay: Long, endOfDay: Long): Flow<List<FoodEntry>>

    @Query("DELETE FROM food_entries WHERE id = :entryId")
    suspend fun deleteFood(entryId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductMetadata(product: ProductMetadata)

    @Query("SELECT * FROM product_metadata WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): ProductMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreset(preset: MealPreset)

    @Query("SELECT * FROM meal_presets ORDER BY name ASC")
    fun getAllPresets(): Flow<List<MealPreset>>

    @Query("SELECT * FROM product_metadata ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductMetadata>>

    @Delete
    suspend fun deletePreset(preset: MealPreset)

    @Update
    suspend fun updatePreset(preset: MealPreset)

    @Update
    suspend fun updateFoodEntry(entry: FoodEntry)

    @Update
    suspend fun updateProductMetadata(product: ProductMetadata)

    @Delete
    suspend fun deleteProduct(product: ProductMetadata)


}