package com.example.zilean

import platform.UIKit.*
import platform.Foundation.*
import kotlinx.cinterop.*
import platform.darwin.NSObject

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val type: PlatformType = PlatformType.IOS
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun shareText(text: String, title: String) {
    val root = UIApplication.sharedApplication.keyWindow?.rootViewController
    if (root != null) {
        val items = listOf(text)
        val activityController = UIActivityViewController(activityItems = items, applicationActivities = null)
        root.presentViewController(activityController, animated = true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class FilePickerDelegate(private val onResult: (String?) -> Unit) : NSObject(), UIDocumentPickerDelegateProtocol {
    override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
        if (url != null) {
            val shouldStopAccessing = url.startAccessingSecurityScopedResource()
            try {
                val content = NSString.stringWithContentsOfURL(url, encoding = NSUTF8StringEncoding, error = null)
                onResult(content)
            } finally {
                if (shouldStopAccessing) url.stopAccessingSecurityScopedResource()
            }
        } else {
            onResult(null)
        }
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onResult(null)
    }
}

private var pickerDelegate: FilePickerDelegate? = null

actual fun openFilePicker(onResult: (String?) -> Unit) {
    val root = UIApplication.sharedApplication.keyWindow?.rootViewController
    if (root != null) {
        val types = listOf("public.text", "public.plain-text", "public.item")
        val picker = UIDocumentPickerViewController(documentTypes = types, inMode = UIDocumentPickerMode.UIDocumentPickerModeImport)
        
        pickerDelegate = FilePickerDelegate(onResult)
        picker.delegate = pickerDelegate
        
        root.presentViewController(picker, animated = true, completion = null)
    }
}
