package com.example.zilean.data

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.room.Room
import androidx.room.RoomDatabase

lateinit var appContext: Context
var activeActivity: ComponentActivity? = null

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = appContext.getDatabasePath("zilean_database")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
