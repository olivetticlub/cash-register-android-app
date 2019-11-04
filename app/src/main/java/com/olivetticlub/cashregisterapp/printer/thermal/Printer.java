package com.olivetticlub.cashregisterapp.printer.thermal;

import android.graphics.Bitmap;

import com.olivetticlub.cashregisterapp.printer.thermal.bluetooth.BluetoothPrinterSocketConnexion;
import com.olivetticlub.cashregisterapp.printer.thermal.textparser.PrinterTextParser;
import com.olivetticlub.cashregisterapp.printer.thermal.textparser.PrinterTextParserBarcode;
import com.olivetticlub.cashregisterapp.printer.thermal.textparser.PrinterTextParserColumn;
import com.olivetticlub.cashregisterapp.printer.thermal.textparser.PrinterTextParserElement;
import com.olivetticlub.cashregisterapp.printer.thermal.textparser.PrinterTextParserImg;
import com.olivetticlub.cashregisterapp.printer.thermal.textparser.PrinterTextParserLine;
import com.olivetticlub.cashregisterapp.printer.thermal.textparser.PrinterTextParserString;

import java.io.IOException;


public class Printer {

    public static final float INCH_TO_MM = 25.4f;

    private int printerDpi;
    private float printingWidthMM;
    private int nbrCharactersPerLine;
    private int printingWidthPx;
    private int charSizeWidthPx;


    private BluetoothPrinterSocketConnexion bluetoothPrinter = null;


    /**
     * Create a new instance of Printer.
     *
     * @param printer              Instance of the bluetooth connexion with the printer
     * @param printerDpi           DPI of the connected printer
     * @param printingWidthMM      Printing width in millimeters
     * @param nbrCharactersPerLine The maximum number of characters that can be printed on a line.
     */
    public Printer(BluetoothPrinterSocketConnexion printer, int printerDpi, float printingWidthMM, int nbrCharactersPerLine) {
        this.bluetoothPrinter = printer;
        this.printerDpi = printerDpi;
        this.printingWidthMM = printingWidthMM;
        this.nbrCharactersPerLine = nbrCharactersPerLine;

        int printingWidthPx = this.mmToPx(this.printingWidthMM);
        this.printingWidthPx = printingWidthPx + (printingWidthPx % 8);

        this.charSizeWidthPx = printingWidthPx / this.nbrCharactersPerLine;
    }

    /**
     * Close the Bluetooth connexion with the printer.
     *
     * @return Fluent interface
     */
    public Printer disconnectPrinter() {
        if (this.bluetoothPrinter != null) {
            this.bluetoothPrinter.disconnect();
            this.bluetoothPrinter = null;
        }
        return this;
    }

    public Boolean isConnected() {
        if (!bluetoothPrinter.isConnected()) {
            bluetoothPrinter.connect();
        }
        return this.bluetoothPrinter.isConnected();
    }

    /**
     * Get the maximum number of characters that can be printed on a line.
     *
     * @return int
     */
    public int getNbrCharactersPerLine() {
        return this.nbrCharactersPerLine;
    }

    /**
     * Get the printing width in millimeters
     *
     * @return float
     */
    public float getPrintingWidthMM() {
        return this.printingWidthMM;
    }

    /**
     * Get the printer DPI
     *
     * @return int
     */
    public int getPrinterDpi() {
        return this.printerDpi;
    }

    /**
     * Get the printing width in dot
     *
     * @return int
     */
    public int getPrintingWidthPx() {
        return this.printingWidthPx;
    }

    /**
     * Get the number of dot that a printed character contain
     *
     * @return int
     */
    public int getCharSizeWidthPx() {
        return this.charSizeWidthPx;
    }

    /**
     * Convert from millimeters to dot the mmSize variable.
     *
     * @param mmSize Distance in millimeters to be converted
     * @return int
     */
    public int mmToPx(float mmSize) {
        return Math.round(mmSize * ((float) this.printerDpi) / Printer.INCH_TO_MM);
    }

    /**
     * Print a formatted text. Read the README.md for more information about text formatting options.
     *
     * @param text Formatted text to be printed.
     * @return Fluent interface
     */

    public Printer printFormattedText(String text) {
        if (this.bluetoothPrinter == null || this.nbrCharactersPerLine == 0) {
            return this;
        }

        PrinterTextParser textParser = new PrinterTextParser(this);
        PrinterTextParserLine[] linesParsed = textParser
                .setFormattedText(text)
                .parse();

        for (PrinterTextParserLine line : linesParsed) {
            PrinterTextParserColumn[] columns = line.getColumns();

            for (PrinterTextParserColumn column : columns) {
                PrinterTextParserElement[] elements = column.getElements();
                for (PrinterTextParserElement element : elements) {
                    if (element instanceof PrinterTextParserString) {
                        PrinterTextParserString string = (PrinterTextParserString) element;
                        this.bluetoothPrinter.printText(string.getText(), string.getTextSize(), string.getTextBold(), string.getTextUnderline());
                    } else if (element instanceof PrinterTextParserImg) {
                        PrinterTextParserImg img = (PrinterTextParserImg) element;
                        this.bluetoothPrinter.printImage(img.getImage());
                    } else if (element instanceof PrinterTextParserBarcode) {
                        PrinterTextParserBarcode barcode = (PrinterTextParserBarcode) element;
                        this.bluetoothPrinter
                                .setAlign(barcode.getAlign())
                                .printBarcode(barcode.getBarcodeType(), barcode.getCode(), barcode.getHeight());
                    }
                }
            }
            this.bluetoothPrinter.newLine();
        }

        return this;
    }

    public Printer printQrCode(String data) {
        if (this.bluetoothPrinter == null || this.nbrCharactersPerLine == 0) {
            return this;
        }

        try {
            this.bluetoothPrinter.printQrCode(data);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return this;
    }

    /**
     * Convert Bitmap object to ESC/POS image.
     *
     * @param bitmap Instance of Bitmap
     * @return Bytes contain the image in ESC/POS command
     */
    public byte[] bitmapToBytes(Bitmap bitmap) {
        boolean isSizeEdit = false;
        int bitmapWidth = bitmap.getWidth(),
                bitmapHeight = bitmap.getHeight(),
                maxWidth = this.getPrintingWidthPx(),
                maxHeight = 256;

        if (bitmapWidth > maxWidth) {
            bitmapHeight = Math.round(((float) bitmapHeight) * ((float) maxWidth) / ((float) bitmapWidth));
            bitmapWidth = maxWidth;
            isSizeEdit = true;
        }
        if (bitmapHeight > maxHeight) {
            bitmapWidth = Math.round(((float) bitmapWidth) * ((float) maxHeight) / ((float) bitmapHeight));
            bitmapHeight = maxHeight;
            isSizeEdit = true;
        }

        if (isSizeEdit) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false);
        }

        return BluetoothPrinterSocketConnexion.bitmapToBytes(bitmap);
    }
}
