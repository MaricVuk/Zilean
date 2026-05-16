package com.example.zilean.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers


@Database(
    entities = [
        FoodEntry::class,
        ProductMetadata::class,
        MealPreset::class,
    ],
    version = 3,
    exportSchema = false
)

@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())


        .setQueryCoroutineContext(Dispatchers.Default)
        .build()

}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
