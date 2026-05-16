package com.example.zilean

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class ZileanSettings(private val settings: Settings) {
    var isDarkMode: Boolean
        get() = settings["is_dark_mode", false]
        set(value) { settings["is_dark_mode"] = value }

    var isSetupDone: Boolean
        get() = settings["is_setup_done", false]
        set(value) { settings["is_setup_done"] = value }

    var userName: String
        get() = settings["user_name", "person"]
        set(value) { settings["user_name"] = value }

    var goalCalories: Int
        get() = settings["goal_calories", 2000]
        set(value) { settings["goal_calories"] = value }

    var goalProtein: Int
        get() = settings["goal_protein", 190]
        set(value) { settings["goal_protein"] = value }

    var goalCarbs: Int
        get() = settings["goal_carbs", 200]
        set(value) { settings["goal_carbs"] = value }

    var goalFats: Int
        get() = settings["goal_fats", 70]
        set(value) { settings["goal_fats"] = value }

    var goalWater: Int
        get() = settings["goal_water", 1000]
        set(value) { settings["goal_water"] = value }
}
