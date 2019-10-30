package com.olivetticlub.cashregisterapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.olivetticlub.cashregisterapp.products.Product
import com.olivetticlub.cashregisterapp.transaction.TransactionFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity(), ProductSelectionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun productSelected(product: Product) {
        (transactionFragment as TransactionFragment).productSelected(product)
    }

}
