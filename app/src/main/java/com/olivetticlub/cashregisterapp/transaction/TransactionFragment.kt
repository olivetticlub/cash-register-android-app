package com.olivetticlub.cashregisterapp.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.olivetticlub.cashregisterapp.ProductSelectionListener
import com.olivetticlub.cashregisterapp.R
import com.olivetticlub.cashregisterapp.printer.thermal.Printer
import com.olivetticlub.cashregisterapp.printer.thermal.bluetooth.BluetoothPrinters
import com.olivetticlub.cashregisterapp.products.Product
import com.olivetticlub.cashregisterapp.products.ProductAdapter
import com.olivetticlub.cashregisterapp.products.ProductAdapter.LayoutType.LIST
import kotlinx.android.synthetic.main.fragment_transaction.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionFragment : Fragment(), ProductSelectionListener {
    private lateinit var printer: Printer
    private var productList = mutableListOf<Product>()
    private var totalAmount = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        printer = Printer(BluetoothPrinters().list.first(), 203, 58.0f, 32)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        productRecyclerView.adapter = ProductAdapter(productList, this, LIST)
        productRecyclerView.layoutManager = LinearLayoutManager(context!!)

        totalAmountTextView.text = formattedTotalAmount()

        printReceiptButton.setOnClickListener {
            if (productList.isNotEmpty()) {
                printReceipt()
            }

        }

    }

    override fun onProductSelected(product: Product) {
        productList.remove(product)
        updateTransaction()
    }

    private fun printReceipt() {
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())

        printer.printFormattedText(
            "[L]\n" +
                    "[C]<font size='tall'>RISTORANTE DA BONTE</font>\n" +
                    "[L]\n" +
                    "[R]EURO\n" +
                    productList.map {
                        "[L]${it.name} [R]${it.priceDescriptionWithoutEur}\n"
                    }.joinToString("") +
                    "[C]--------------------------------\n" +
                    "[L]<font size='tall'>TOTALE</font>[R]${formattedTotalAmount()}\n" +
                    "[L]\n" +
                    "[C]${date}\n" +
                    "[L]\n" +
                    "[C]================================\n" +
                    "[L]\n"
        ).printQrCode("Cazzettino")
            .printFormattedText(
                "[L]\n" +
                        "[C]Sconto del 1% sul Cazzettino\n" +
                        "[C]<b>Cazzettino Shop</b>\n" +
                        "[C] Via dalle palle 11\n" +
                        "[C]Trento 38122 Italia\n"
            )
            .printFormattedText("[L]\n[L]\n[L]\n")
    }

    fun productSelected(product: Product) {
        productList.add(product)
        updateTransaction()
    }

    private fun updateTransaction() {
        productRecyclerView.adapter?.notifyDataSetChanged()

        totalAmount = if (productList.isNotEmpty()) {
            productList.map { (it.price.toFloat() / 100) }
                .reduce { sum, price -> sum + price }
        } else {
            0.0f
        }
        
        totalAmountTextView.text = formattedTotalAmount()
    }

    private fun formattedTotalAmount() = "%.2f".format(totalAmount)

}