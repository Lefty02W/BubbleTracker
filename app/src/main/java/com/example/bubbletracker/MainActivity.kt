package com.example.bubbletracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOpenPersonalCardActivity: Button = findViewById(R.id.personalCardBtn)
        val btnOpenBusinessCardActivity: Button = findViewById(R.id.businessCardBtn)
        val btnOpenScannerActivity: Button = findViewById(R.id.scannerBtn)

        btnOpenPersonalCardActivity.setOnClickListener {
            val openPersonalCard = Intent(this, PersonalCardActivity::class.java)
            startActivity(openPersonalCard)
        }

        btnOpenBusinessCardActivity.setOnClickListener {
            val openBusinessCardActivity = Intent(this, BusinessCardActivity::class.java)
            startActivity(openBusinessCardActivity)
        }

        btnOpenScannerActivity.setOnClickListener {
            val openScannerActivity = Intent(this, ScannerActivity::class.java)
            startActivity(openScannerActivity)
        }
    }
}