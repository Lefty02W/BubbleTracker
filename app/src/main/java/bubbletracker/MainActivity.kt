package bubbletracker

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
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
        createNotificationChannel()
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val channelId = getString(R.string.channel_id)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        val notification = Notification.Builder(this, Notification.CATEGORY_REMINDER).run {
                            setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                            setContentTitle("A new day, a new memory")
                            setContentText("Just a friendly reminder to take today's picture.")
                            setAutoCancel(true)
                            build()
                        }

                        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.notify(0, notification)
                    }
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