package ITI.covid19_tracker.db.FetchingAPIData.StatisticData

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class getContextActivity : AppCompatActivity() {

    lateinit var mycontect : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mycontect = applicationContext
    }
}