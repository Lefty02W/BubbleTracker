package bubbletracker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.bubbletracker.R
import com.google.zxing.integration.android.IntentIntegrator
import data.db.entity.Connection
import data.db.viewModel.BubbleViewModel

class MainActivity : AppCompatActivity(){

    private lateinit var bubbleViewModel: BubbleViewModel
    private var notificationManager: NotificationManager? = null

    private fun checkTheme(){
        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme)
        } else setTheme(R.style.AppTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        checkTheme()
        super.onCreate(savedInstanceState)
        bubbleViewModel = BubbleViewModel(application)
        setContentView(R.layout.activity_main)
//        createNotificationChannel()
        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                "BubbleTracker",
                "Connection Added",
                 "successfully connected")
        }

        runAnimation()
        initButtons()

        bubbleViewModel.allConnections.observe(this, Observer {
            val directConnectionNumber = it.size
            findViewById<TextView>(R.id.directConnectionTotal).apply {
                text = directConnectionNumber.toString()
            }
            var indirectConnection = 0
            for (connection in it.listIterator()){
                indirectConnection += connection.directConnections
            }
            findViewById<TextView>(R.id.indirectConnectionTotal).apply {
                text = indirectConnection.toString()
            }
        })
    }

    private fun initButtons(){
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add_action){
            val openPreferences = Intent(this, SettingsActivity::class.java)
            startActivity(openPreferences)
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            vibrate()
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
                    addConnection(result.contents)
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun vibrate(){
        val vibrator: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    200,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }

    }

    private fun addConnection(contents: String) {
        val result: List<String> = contents.split(",")
        //todo send notification with "%result[0] added to connections
        val name = result[0]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotification(name)
        }
        bubbleViewModel.getConnectionOnEmail(result[4]).observe(this, Observer {
                 if (it.isEmpty()){
                    bubbleViewModel.insertConnection(Connection(result[1].toInt(), result[2],result[3],result[4]))
                } else {
                bubbleViewModel.updateConnection(Connection(result[1].toInt(), result[2],result[3],result[4]))
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(name:String) {
        val notificationID = 101
        val channelID = "BubbleTracker"
        val notification =
            Notification.Builder(this,
                channelID)
                .setContentTitle("Connection Successful")
                .setContentText("$name added to connections.")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .build()

        notificationManager?.notify(notificationID, notification)
    }

    override fun onResume() {
        checkTheme()
        runAnimation()
        super.onResume()
    }

    override fun onStart() {
        checkTheme()
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

    }

}