package com.example.dscanner.data.model

data class ProductCardState(
    var showBarcodeDialog: Boolean = false,
    var showPriceDialog: Boolean = false,
    var showCompetitorDialog: Boolean = false,
    var rawInput: String = "0",
    var newCompetitorName: String = ""
)
