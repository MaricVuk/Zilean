package com.example.zilean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.zilean.data.appContext

import androidx.activity.result.contract.ActivityResultContracts
import com.example.zilean.data.activeActivity

class MainActivity : ComponentActivity() {
    private var filePickerCallback: ((String?) -> Unit)? = null

    private val pickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val content = contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
            filePickerCallback?.invoke(content)
        } else {
            filePickerCallback?.invoke(null)
        }
        filePickerCallback = null
    }

    fun launchFilePicker(callback: (String?) -> Unit) {
        filePickerCallback = callback
        pickerLauncher.launch("*/*")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        activeActivity = this
        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}
