package com.example.bubbletracker

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScannerActivity: Activity(), ZBarScannerView.ResultHandler {

    private lateinit var mScannerView: ZBarScannerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZBarScannerView(this)
//        setContentView(R.layout.activity_scanner)
        setContentView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Toast.makeText(this, rawResult?.contents, Toast.LENGTH_SHORT).show()
        mScannerView.resumeCameraPreview(this)
    }
}