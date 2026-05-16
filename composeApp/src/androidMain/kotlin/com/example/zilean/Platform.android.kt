package com.example.zilean

import android.os.Build
import android.content.Intent
import com.example.zilean.data.activeActivity
import com.example.zilean.data.appContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val type: PlatformType = PlatformType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun shareText(text: String, title: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, text)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    val chooser = Intent.createChooser(intent, title).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    appContext.startActivity(chooser)
}

actual fun openFilePicker(onResult: (String?) -> Unit) {
    val activity = activeActivity as? MainActivity
    if (activity != null) {
        activity.launchFilePicker(onResult)
    } else {
        onResult(null)
    }
}
