package com.example.zilean

interface Platform {
    val name: String
    val type: PlatformType
}

enum class PlatformType {
    ANDROID, IOS
}

expect fun getPlatform(): Platform

expect fun shareText(text: String, title: String)

expect fun openFilePicker(onResult: (String?) -> Unit)