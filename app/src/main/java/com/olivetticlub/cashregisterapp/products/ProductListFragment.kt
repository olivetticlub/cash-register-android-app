package com.olivetticlub.cashregisterapp.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.olivetticlub.cashregisterapp.R
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val productList = mutableListOf(
            Product("Coperto", 150, R.mipmap.coperto),
            Product("Antipasto", 800, R.mipmap.antipasto),
            Product("Patatine", 250, R.mipmap.patatine),
            Product("Pizza", 500, R.mipmap.pizza),
            Product("Pasta", 600, R.mipmap.pasta),
            Product("Tagliata di manzo", 1300, R.mipmap.tagliata),
            Product("Frittura di Pesce", 1500, R.mipmap.frittura),
            Product("Grigliata di Carne", 1400, R.mipmap.grigliata),
            Product("Acqua", 250, R.mipmap.acqua),
            Product("Bevanda", 400, R.mipmap.cola),
            Product("Birra", 450, R.mipmap.birra),
            Product("Vino", 450, R.mipmap.vino),
            Product("Tiramisù", 300, R.mipmap.tiramisu),
            Product("Panna cotta", 300, R.mipmap.panna_cotta)
        )

        productGridView.adapter = ProductAdapter(productList)
        productGridView.layoutManager = GridLayoutManager(context!!, 4)
    }

}