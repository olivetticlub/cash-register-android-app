package com.olivetticlub.cashregisterapp.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olivetticlub.cashregisterapp.R
import kotlinx.android.synthetic.main.product_list_item.view.*

class ProductAdapter(private val productList: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.product_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.nameTextView.text = productList[position].name
        holder.view.priceTextView.text = productList[position].price
        holder.view.imageView.setImageResource(productList[position].image)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}