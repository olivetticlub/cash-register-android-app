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
            Product("Pizza", "5.00€", R.mipmap.pizza),
            Product("Pizza", "5.00€", R.mipmap.pizza),
            Product("Pizza", "5.00€", R.mipmap.pizza),
            Product("Pizza", "5.00€", R.mipmap.pizza),
            Product("Pizza", "5.00€", R.mipmap.pizza)
        )

        productGridView.adapter = ProductAdapter(productList)
        productGridView.layoutManager = GridLayoutManager(context!!, 5)
    }

}