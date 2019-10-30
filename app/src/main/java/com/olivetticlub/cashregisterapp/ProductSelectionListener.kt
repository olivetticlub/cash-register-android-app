package com.olivetticlub.cashregisterapp

import com.olivetticlub.cashregisterapp.products.Product

interface ProductSelectionListener {
    fun onProductSelected(product: Product)
}
