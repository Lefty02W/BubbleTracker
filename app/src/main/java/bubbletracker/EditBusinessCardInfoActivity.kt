package bubbletracker

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.example.bubbletracker.R
import data.db.entity.UserData
import data.db.viewModel.BubbleViewModel

class EditBusinessCardInfoActivity: AppCompatActivity() {
    lateinit var bubbleViewModel: BubbleViewModel
    lateinit var personalName: String
    lateinit var personalEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme)
        } else setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_business_info)

        bubbleViewModel = BubbleViewModel(application)


        bubbleViewModel.currentUser.observe(this, Observer {
            findViewById<EditText>(R.id.businessNameInput).apply {
                setText(it?.businessName.orEmpty())
            }
            findViewById<EditText>(R.id.emailInput).apply {
                setText(it?.businessEmail.orEmpty())
            }
            personalName = it?.personalName.orEmpty()
            personalEmail = it?.personalEmail.orEmpty()
        })


        val btnSaveUserData: Button = findViewById(R.id.saveUserDataBtn)

        btnSaveUserData.setOnClickListener {
            val newName = findViewById<EditText>(R.id.businessNameInput).text
            val newEmail = findViewById<EditText>(R.id.emailInput).text
            bubbleViewModel.upsertUser(
                UserData(personalName, personalEmail,
                    newEmail.toString(), newName.toString()
                )
            )
            onBackPressed()
        }
    }
}