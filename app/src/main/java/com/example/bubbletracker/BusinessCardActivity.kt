package com.example.bubbletracker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.activity_personal_card.*
import java.lang.IllegalArgumentException

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
        val bitmap = encodeAsBitmap(text)
        personalQR.setImageBitmap(bitmap)
    }

    private fun encodeAsBitmap(text: String): Bitmap?{
        val result: BitMatrix
        try{
            result = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300, null)
        } catch (e: IllegalArgumentException){
            return null
        }
        val resultWidth = result.width
        val resultHeight = result.height
        val pixels = IntArray(resultWidth*resultHeight)
        for (y in 0 until resultHeight){
            val offset = y * resultWidth
            for (x in 0 until resultWidth){
                //If true then dark pixel if false then
                pixels[offset + x] = if (result.get(x,y)) - 0x1000000 else -0x1
            }
        }
        val bitmap = Bitmap.createBitmap(resultWidth,resultHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, resultWidth, 0, 0, resultWidth, resultHeight)
        return bitmap
    }
}