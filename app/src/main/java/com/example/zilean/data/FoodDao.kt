package com.example.zilean.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: FoodEntry)

    @Query("SELECT * FROM food_entries ORDER BY date DESC")
    fun getAllFood(): Flow<List<FoodEntry>>

    @Query("SELECT SUM(protein) FROM food_entries WHERE date >= :startOfDay")
    fun getTodaysProtein(startOfDay: Long): Flow<Int?>

    @Query ("SELECT SUM(calories) FROM food_entries WHERE date >= :startOfDay")
    fun getTodaysCalories(startOfDay: Long): Flow<Int?>

    @Query ("SELECT SUM(fats) FROM food_entries WHERE date >= :startOfDay")
    fun getTodaysFats(startOfDay: Long): Flow<Int?>

    @Query ("SELECT SUM(carbs) FROM food_entries WHERE date >= :startOfDay")
    fun getTodaysCarbs(startOfDay: Long): Flow<Int?>

    @Query("SELECT * FROM food_entries WHERE date >= :startOfDay ORDER BY date DESC")
    fun getTodaysFoodList(startOfDay: Long): Flow<List<FoodEntry>>

    @Query("DELETE FROM food_entries WHERE id = :entryId")
    suspend fun deleteFood(entryId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductMetadata(product: ProductMetadata)

    @Query("SELECT * FROM product_metadata WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): ProductMetadata?

}