package bubbletracker

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.bubbletracker.R

class EditBusinessCardInfoActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme)
        } else setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_business_info)
    }
}