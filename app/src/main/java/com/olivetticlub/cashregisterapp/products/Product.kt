package com.olivetticlub.cashregisterapp.products

data class Product(val name: String, val price: Int, val image: Int) {
    val priceDescription: String
        get() {
            return "${"%.2f".format((price.toFloat() / 100))} €"
        }
}