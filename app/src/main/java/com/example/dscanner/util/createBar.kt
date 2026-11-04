package com.example.dscanner.util

import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

fun createBar(code: String): Bitmap {
    return BarcodeEncoder().encodeBitmap(code, BarcodeFormat.EAN_13, 600, 300)
}

fun sanitizeEAN13(inputCode: String): String {
    if (!inputCode.matches(Regex("\\d{13}"))) {
        throw IllegalArgumentException("Código deve ter 13 dígitos numéricos")
    }

    val base = inputCode.substring(0, 12)
    val expectedChecksum = calculateEAN13ChecksumDigit(base)
    return if (inputCode.last() == expectedChecksum) {
        inputCode
    } else {
        base + expectedChecksum
    }
}

fun calculateEAN13ChecksumDigit(ean12: String): Char {
    if (ean12.length != 12 || !ean12.all { it.isDigit() }) {
        throw IllegalArgumentException("EAN-13 requer exatamente 12 dígitos numéricos.")
    }

    val sum = ean12.mapIndexed { index, c ->
        val digit = c.toString().toInt()
        if (index % 2 == 0) digit else digit * 3
    }.sum()

    val checksum = (10 - (sum % 10)) % 10
    return checksum.toString().first()
}