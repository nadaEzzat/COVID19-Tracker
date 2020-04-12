package ITI.covid19_tracker.settings

import ITI.covid19_tracker.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class settingsMain2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_main2)
        supportActionBar!!.title = "Settings"

    }
}
