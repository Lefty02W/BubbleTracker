package com.example.bubbletracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

//import me.dm7.barcodescanner.zbar.Result
//import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScannerActivity: Activity(){

//    private lateinit var mScannerView: ZBarScannerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mScannerView = ZBarScannerView(this)
        setContentView(R.layout.activity_scanner)
        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        scanner.setBeepEnabled(false)
        scanner.initiateScan()
//        setContentView(mScannerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
//    override fun onResume() {
//        super.onResume()
//       // mScannerView.startCamera()
//    }

//    override fun onPause() {
//        super.onPause()
//       // mScannerView.stopCamera()
//    }

//    override fun handleResult(rawResult: Result?) {
//        Toast.makeText(this, rawResult?.contents, Toast.LENGTH_SHORT).show()
//        mScannerView.resumeCameraPreview(this)
//    }
}