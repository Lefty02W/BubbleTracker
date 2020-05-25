package bubbletracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.bubbletracker.R
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runAnimation()

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
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
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

    override fun onResume() {
        runAnimation()
        super.onResume()
    }

    override fun onStart() {
        runAnimation()
        super.onStart()
    }

    private fun runAnimation() {
        val btt: Animation = AnimationUtils.loadAnimation(this, R.anim.btt)
        val btt2: Animation = AnimationUtils.loadAnimation(this, R.anim.btt2)
        val btt3: Animation = AnimationUtils.loadAnimation(this, R.anim.btt3)

        val personalCardBtn: TextView = findViewById(R.id.personalCardBtn)
        val businessCardBtn: TextView = findViewById(R.id.businessCardBtn)
        val scannerBtn: TextView = findViewById(R.id.scannerBtn)
        personalCardBtn.startAnimation(btt3)
        businessCardBtn.startAnimation(btt2)
        scannerBtn.startAnimation(btt)
//        val ttb2: Animation = AnimationUtils.loadAnimation(this, R.anim.ttb2)
//        val stb: Animation = AnimationUtils.loadAnimation(this, R.anim.stb)

//        val logo: ImageView = findViewById(R.id.logoImage)
//        val rudimentBtn: TextView = findViewById(R.id.rudiment_btn)
//        val metronomeBtn: TextView = findViewById(R.id.metronome_btn)
//
//        logo.startAnimation(stb)
//        rudimentBtn.startAnimation(ttb)
//        metronomeBtn.startAnimation(ttb2)
    }

}