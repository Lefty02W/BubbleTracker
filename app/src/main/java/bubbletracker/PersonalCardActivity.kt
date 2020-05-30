package bubbletracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.bubbletracker.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import data.db.viewModel.BubbleViewModel
import kotlinx.android.synthetic.main.activity_personal_card.*
import java.lang.IllegalArgumentException

class PersonalCardActivity: AppCompatActivity() {

    private val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var lat = ""
    var long = ""
    var name = ""
    var email = ""
    var directConnectionTotal = ""
    lateinit var generateButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme)
        } else setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_card)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        val bubbleViewModel = BubbleViewModel(application)
        bubbleViewModel.currentUser.observe(this, Observer {
            name = it?.personalName.orEmpty()
            email = it?.personalEmail.orEmpty()
        })

        bubbleViewModel.allConnections.observe(this, Observer {
            directConnectionTotal = it.size.toString()
        })

        val btnOpenEditPersonalCardInfoActivity: Button = findViewById(R.id.pCardEditBtn)
        btnOpenEditPersonalCardInfoActivity.setOnClickListener {
            val openEditPersonalCardInfoActivity = Intent(this, EditPersonalCardInfoActivity::class.java)
            startActivity(openEditPersonalCardInfoActivity)
        }
        generateButton = findViewById(R.id.pCardGenerateBtn)
        generateButton.setOnClickListener {
            generatePersonalQR()
        }
        checkData()
    }

    private fun checkData() {
        if (name.isNotEmpty() && email.isNotEmpty()){
            generatePersonalQR()
        }
    }

    private fun generatePersonalQR() {
        if (name != "" && email != ""){
            val sentData = "$name,$directConnectionTotal,$long,$lat,$email"
            val bitmap = encodeAsBitmap(sentData)
            businessQR.setImageBitmap(bitmap)
        }
    }

    private fun encodeAsBitmap(text: String): Bitmap?{
        val result: BitMatrix
        try{
            result = MultiFormatWriter().encode(text,BarcodeFormat.QR_CODE, 300, 300, null)
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

    private fun checkLocationPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
                getLastLocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

//    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkLocationPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        lat = "latitude not recorded"
                        long = "longitude not recorded"
                    } else {
                        lat = location.latitude.toString()
                        long = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    override fun onResume() {
        checkData()
        super.onResume()
    }

    override fun onStart() {
        checkData()
        super.onStart()
    }

    /**
     * Method to aviod rare cases of location == null this method records the location information
     * during run time.
     */
//    private fun requestNewLocationData() {
//        var mLocationRequest = LocationRequest()
//        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest.interval = 0
//        mLocationRequest.fastestInterval = 0
//        mLocationRequest.numUpdates = 1
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        mFusedLocationClient!!.requestLocationUpdates(
//            mLocationRequest, mLocationCallback,
//            Looper.myLooper()
//        )
//    }

//    private fun promptForGPS() {
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            AlertDialog.Builder(this).apply {
//                setMessage("GPS is not enabled on your device. Enable it in the location settings.")
//                setPositiveButton("Settings") { _, _ ->
//                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                }
//                setNegativeButton("Cancel") { _, _ -> }
//                show()
//            }
//        }
//    }

}