package com.example.zilean

import com.example.zilean.data.ProductMetadata
import com.example.zilean.data.appContext
import com.example.zilean.data.getDatabaseBuilder
import com.example.zilean.data.getRoomDatabase
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AndroidScanner : Scanner {
    override fun scanBarcode(onProductFound: (ProductMetadata?, String) -> Unit) {
        val scanner = GmsBarcodeScanning.getClient(appContext)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val code = barcode.rawValue ?: return@addOnSuccessListener
                
                CoroutineScope(Dispatchers.IO).launch {
                    val db = getRoomDatabase(getDatabaseBuilder())
                    val metadata = db.foodDao().getProductByBarcode(code)
                    onProductFound(metadata, code)
                }
            }
    }
}

actual fun getScanner(): Scanner = AndroidScanner()
