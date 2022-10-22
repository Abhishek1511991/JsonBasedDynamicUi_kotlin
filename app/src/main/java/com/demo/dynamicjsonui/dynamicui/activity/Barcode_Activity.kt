package com.demo.dynamicjsonui.dynamicui.activity

import androidx.appcompat.app.AppCompatActivity
import me.dm7.barcodescanner.zbar.ZBarScannerView
import android.os.Bundle
import android.util.Log
import me.dm7.barcodescanner.zbar.Result

/**
 * Created by Abhishek at 20/10/2022
 */
class Barcode_Activity : AppCompatActivity(), ZBarScannerView.ResultHandler {
    private var mScannerView: ZBarScannerView? = null
    var pid: String? = null
    var cid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZBarScannerView(this) // Programmatically initialize the scanner view
        setContentView(mScannerView)
        pid = intent.getStringExtra("pkey")
        cid = intent.getStringExtra("ckey")
        mScannerView!!.setAutoFocus(true)

        // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera() // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera() // Stop camera on pause
    }

    override fun handleResult(result: Result?) {
        result?.getContents()?.let { Log.v("barcode_scanner", it) } // Prints scan results
        result?.getBarcodeFormat()?.getName()?.let {
            Log.v(
                "barcode_scanner",
                it
            )
        }
        finish()
    }
}