package com.olivetticlub.cashregisterapp.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.olivetticlub.cashregisterapp.R
import com.olivetticlub.cashregisterapp.printer.thermal.Printer
import com.olivetticlub.cashregisterapp.printer.thermal.bluetooth.BluetoothPrinters
import kotlinx.android.synthetic.main.fragment_transaction.*

class TransactionFragment : Fragment() {

    private lateinit var printer: Printer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        printer = Printer(BluetoothPrinters().list.first(), 203, 58.0f, 42)
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


        printReceiptButton.setOnClickListener {
            printer.printFormattedText("[L]1 x Cazzettino\n")
        }
    }

}