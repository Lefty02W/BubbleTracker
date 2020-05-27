package bubbletracker

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.bubbletracker.R
import data.db.entity.UserData
import data.db.viewModel.BubbleViewModel
import kotlin.NullPointerException

class EditPersonalCardInfoActivity: AppCompatActivity() {
    lateinit var bubbleViewModel: BubbleViewModel
    lateinit var businessName: String
    lateinit var businessEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal_info)

        bubbleViewModel = BubbleViewModel(application)


//        try {
        bubbleViewModel.currentUser.observe(this, Observer {
            findViewById<EditText>(R.id.personalNameInput).apply {
                setText(it?.personalName.orEmpty())
            }
            findViewById<EditText>(R.id.emailInput).apply {
                setText(it?.personalEmail.orEmpty())
            }
            businessName = it?.businessName.orEmpty()
            businessEmail = it?.businessEmail.orEmpty()
            })
//        } catch (exception: NullPointerException){
//            findViewById<EditText>(R.id.personalNameInput).apply {
//                setText("")
//            }
//            findViewById<EditText>(R.id.emailInput).apply {
//                setText("")
//            }
//            businessName = ""
//            businessEmail = ""
//        }

        val btnSaveUserData: Button = findViewById(R.id.saveUserDataBtn)

        btnSaveUserData.setOnClickListener {
            val newName = findViewById<EditText>(R.id.personalNameInput).text
            val newEmail = findViewById<EditText>(R.id.emailInput).text
            bubbleViewModel.upsertUser(
                UserData(
                    newEmail.toString(), newName.toString(),
                    businessEmail, businessName
                )
            )
        }
    }
}