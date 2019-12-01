package com.olivetticlub.cashregisterapp.transaction

import com.olivetticlub.cashregisterapp.printer.thermal.Printer
import com.olivetticlub.cashregisterapp.products.Product
import com.olivetticlub.cashregisterapp.services.CouponGeneration
import com.olivetticlub.cashregisterapp.services.Deal
import java.text.SimpleDateFormat
import java.util.*

class TransactionPresenter(private val service: CouponGeneration) {

    private var view: TransactionView? = null

    fun attach(view: TransactionView) {
        this.view = view
    }

    fun detach() {
        this.view = null
    }

    fun printReceiptButtonClicked(productList: List<Product>, amount: Float, printer: Printer?) {
        service.retrieveCoupon("demo-merchant") { coupon ->
            coupon?.let { deal ->
                printReceipt(productList, amount, deal, printer)
            }
        }
    }

    private fun printReceipt(
        productList: List<Product>,
        amount: Float,
        deal: Deal?,
        printer: Printer?
    ) {
        if (printer != null && printer.isConnected!!) {

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
                        "[L]<font size='tall'>TOTALE</font>[R]${formattedTotalAmount(amount)}\n" +
                        "[L]\n" +
                        "[C]${date}\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n"+
                        "[C]Negozio membro di Olivetti Club:\n" +
                        "[C]Hai vinto un buono sconto!\n" +
                        "[L]\n"
            )

            if (deal != null) {
                printCoupon(printer, deal)
            } else {
                printCoupon(printer, BACKUP_DEAL)
            }

            view?.resetTransaction()
            return
        }

        view?.showPrinterNotConnectedError()
    }

    private fun printCoupon(printer: Printer, deal: Deal) {
        printer
            .printQrCode(deal.id.toString())
            .printFormattedText(
                "[L]\n" +
                        "[C]${deal.description}\n" +
                        "[C]<b>${deal.merchant}</b>\n" +
                        "[C]${deal.merchantAddress}\n"
            )
            .printFormattedText("[L]\n[L]\n[L]\n")
    }


    private fun formattedTotalAmount(amount: Float) = "%.2f".format(amount)

    companion object {
        private val BACKUP_DEAL =
            Deal(
                999,
                "Coupond di backup",
                "Merchant di backup",
                "Via del backup"
            )
    }


}