package com.example.zilean

fun <T> platformValue(android: T, ios: T): T {
    return when (getPlatform().type) {
        PlatformType.ANDROID -> android
        PlatformType.IOS -> ios
    }
}
