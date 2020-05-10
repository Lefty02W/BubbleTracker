package com.example.bubbletracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class PersonalCardActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_card)

        val btnOpenEditPersonalCardInfoActivity: Button = findViewById(R.id.pCardEditBtn)
        btnOpenEditPersonalCardInfoActivity.setOnClickListener {
            val openEditPersonalCardInfoActivity = Intent(this, EditPersonalCardInfoActivity::class.java)
            startActivity(openEditPersonalCardInfoActivity)
        }
    }
}