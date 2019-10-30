package com.olivetticlub.cashregisterapp.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olivetticlub.cashregisterapp.ProductSelectionListener
import com.olivetticlub.cashregisterapp.R
import com.olivetticlub.cashregisterapp.products.ProductAdapter.LayoutType.GRID
import com.olivetticlub.cashregisterapp.products.ProductAdapter.LayoutType.LIST
import kotlinx.android.synthetic.main.product_grid_item.view.*

class ProductAdapter(
    private val productList: MutableList<Product>,
    private val listener: ProductSelectionListener?,
    private val layout: LayoutType
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    companion object {
        private const val LAST_ELEMENT_OF_LIST = 1001
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                when (layout) {
                    GRID -> R.layout.product_grid_item
                    LIST -> when (viewType) {
                        LAST_ELEMENT_OF_LIST -> R.layout.product_list_last_item
                        else -> R.layout.product_list_item
                    }
                },
                parent,
                false
            ),
            listener
        )
    }

    override fun getItemViewType(position: Int): Int {

        if (position == productList.size - 1) {
            return LAST_ELEMENT_OF_LIST
        }

        return -1

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.nameTextView.text = productList[position].name
        holder.view.priceTextView.text = productList[position].priceDescription
        holder.view.imageView.setImageResource(productList[position].image)

        holder.view.setOnClickListener {
            holder.listener?.productSelected(productList[position])
        }
    }

    class ViewHolder(
        val view: View,
        val listener: ProductSelectionListener?
    ) : RecyclerView.ViewHolder(view)

    enum class LayoutType {
        GRID,
        LIST
    }

}