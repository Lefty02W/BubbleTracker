package bubbletracker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import androidx.lifecycle.Observer
import com.example.bubbletracker.R
import com.google.zxing.integration.android.IntentIntegrator
import data.db.entity.Connection
import data.db.viewModel.BubbleViewModel

class MainActivity : AppCompatActivity(){

    private lateinit var bubbleViewModel: BubbleViewModel
    private var notificationManager: NotificationManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
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

        bubbleViewModel.allConnections.observe(this, Observer {
            val directConnectionNumber = it.size
            findViewById<TextView>(R.id.directConnectionTotal).apply {
                text = directConnectionNumber.toString()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        if (id == R.id.add_action){
            Toast.makeText(this, "item Add Clicked", Toast.LENGTH_SHORT).show()
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

//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val channelId = getString(R.string.channel_id)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(channelId, name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

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
                    addConnection(result.contents)
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
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

    }

}