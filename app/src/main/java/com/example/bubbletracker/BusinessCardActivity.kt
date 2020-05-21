package com.example.bubbletracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class BusinessCardActivity: Activity(){

    val text = "this is my business text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_card)

        val btnOpenEditBusinessCardInfoActivity: Button = findViewById(R.id.bCardEditBtn)
        btnOpenEditBusinessCardInfoActivity.setOnClickListener {
            val openEditBusinessCardInfoActivity = Intent(this, EditBusinessCardInfoActivity::class.java)
            startActivity(openEditBusinessCardInfoActivity)
        }

        val btnGenerateBusinessQR: Button = findViewById(R.id.bCardGenerateBtn)
        btnGenerateBusinessQR.setOnClickListener {
            generateBusinessQR()
        }
    }

    private fun generateBusinessQR() {

    }
}