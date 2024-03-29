package com.olivetticlub.cashregisterapp.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.olivetticlub.cashregisterapp.ProductSelectionListener
import com.olivetticlub.cashregisterapp.R
import com.olivetticlub.cashregisterapp.printer.thermal.Printer
import com.olivetticlub.cashregisterapp.printer.thermal.bluetooth.BluetoothPrinters
import com.olivetticlub.cashregisterapp.products.Product
import com.olivetticlub.cashregisterapp.products.ProductAdapter
import com.olivetticlub.cashregisterapp.products.ProductAdapter.LayoutType.LIST
import com.olivetticlub.cashregisterapp.services.OlivettiClubService
import kotlinx.android.synthetic.main.fragment_transaction.*

class TransactionFragment : Fragment(), ProductSelectionListener, TransactionView {
    private var printer: Printer? = null
    private var productList = mutableListOf<Product>()
    private var totalAmount = 0.0f
    private var presenter: TransactionPresenter? = null

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

        trashButton.setOnClickListener {
            resetTransaction()
        }

        initPresenter()

        printReceiptButton.setOnClickListener {
            if (productList.isNotEmpty()) {
                presenter?.printReceiptButtonClicked(productList, totalAmount, printer)
            }
        }

        printerStatusImageView.setOnClickListener {
            checkConnection()
        }

    }

    private fun initPresenter() {
        if (presenter == null) {
            presenter = TransactionPresenter(OlivettiClubService("https://olivetti-backend.herokuapp.com/"))
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.attach(this)
    }

    override fun onResume() {
        super.onResume()

        checkConnection()
    }

    override fun onStop() {
        super.onStop()
        presenter?.detach()
    }

    override fun resetTransaction() {
        productList.clear()
        updateTransaction()
    }

    override fun showPrinterNotConnectedError() {
        Toast.makeText(context!!, "Stampante non connessa", Toast.LENGTH_LONG).show()
    }

    override fun onProductSelected(product: Product) {
        productList.remove(product)
        updateTransaction()
    }

    private fun checkConnection() {
        if (printer == null) {
            BluetoothPrinters().list?.let { list ->
                list.firstOrNull {
                    it.device.address == OUR_POOR_PRINTER_ADDRESS
                }?.let {
                    printer = Printer(it, 203, 58.0f, 32)
                }
            }

        }

        if (printer != null && printer?.isConnected!!) {
            printerStatusImageView.setImageResource(R.drawable.ic_connected_printer)
            return
        }

        printerStatusImageView.setImageResource(R.drawable.ic_disconnected_printer)
    }

    fun productSelected(product: Product) {
        productList.add(product)
        updateTransaction()
    }

    private fun updateTransaction() {
        productRecyclerView.adapter?.notifyDataSetChanged()
        productRecyclerView.scrollToPosition(productList.lastIndex)

        totalAmount = if (productList.isNotEmpty()) {
            productList.map { (it.price.toFloat() / 100) }
                .reduce { sum, price -> sum + price }
        } else {
            0.0f
        }

        totalAmountTextView.text = formattedTotalAmount()
    }

    private fun formattedTotalAmount() = "%.2f".format(totalAmount)

    companion object {
        private const val OUR_POOR_PRINTER_ADDRESS = "66:12:E9:E9:FD:54"
    }

}
