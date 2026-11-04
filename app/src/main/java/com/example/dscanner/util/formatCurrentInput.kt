package com.example.dscanner.util

fun formatCurrencyInput(input: String): String {
    val digits = input.filter { it.isDigit() }
    val parsed = digits.toLongOrNull() ?: return ""
    val cents = parsed / 100.0
    return "R$ " + String.format("%,.2f", cents).replace('.', ',')
}