package com.example.zilean

import com.example.zilean.data.ProductMetadata

interface Scanner {
    fun scanBarcode(onProductFound: (ProductMetadata?, String) -> Unit)
}

expect fun getScanner(): Scanner
