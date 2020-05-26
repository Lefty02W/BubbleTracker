package bubbletracker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.bubbletracker.R
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : Activity(){

    var database: ConnectionDatabase? = null
        set(value) {
            field = value
            value?.let {
                LoadConnectionsTask(it, this).execute()
                LoadUserTask(it, this).execute()
            }
        }
    init {
        //todo put this back into the method and only pass context in
        //todo this is where the null pointer is
        val context: Context = applicationContext
        LoadDatabaseTask(this, context).execute()
    }
    var connections: MutableList<Connection> = mutableListOf()
    var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOpenPersonalCardActivity: Button = findViewById(R.id.personalCardBtn)
        val btnOpenBusinessCardActivity: Button = findViewById(R.id.businessCardBtn)
        val btnOpenScannerActivity: Button = findViewById(R.id.scannerBtn)

        if (userData == null){
            userData = UserData("","","","")
        }

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

    fun insertConnection(connection: Connection) {
        if (database != null){
            NewConnectionTask(database!!, connection).execute()
        }
    }

    fun getItemCount(): Int {
        return connections.size
    }
}