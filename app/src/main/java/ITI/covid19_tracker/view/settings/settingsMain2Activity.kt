package ITI.covid19_tracker.view.settings

import ITI.covid19_tracker.R
import ITI.covid19_tracker.WorkManager.UploadWorker
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_settings_main2.*
import java.util.concurrent.TimeUnit

class settingsMain2Activity : AppCompatActivity() {
    var sharedPref: SharedPreferences? = null
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "work-manager"


    companion object{
        private var PRIVATE_MODE = 0
        private val PREF_NAME = "work-manager"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_main2)
        sharedPref=  applicationContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        supportActionBar!!.title = "Settings"

        val sharedPref: SharedPreferences = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var ttttt = sharedPref.getInt("Time",15)
        var u = sharedPref.getString("Unit","MINUTES")

        Time.text =  Editable.Factory.getInstance().newEditable(ttttt.toString())
        if (u.equals("MINUTES")) {

            spinner.setSelection(0)

        } else if (u.equals("DAYS")) {
            spinner.setSelection(2)

        } else if (u.equals("HOURS")) {

            spinner.setSelection(1)

        }


        Save.setOnClickListener {

            var time  = Time.text.toString()
            var unit = spinner.selectedItem.toString()

            Log.i("tag", time)
            Log.i("tag", unit)

            if(time.trim().equals(""))
            {
                Toast.makeText(baseContext ,"Enter Time", Toast.LENGTH_LONG)
                    .show();
            }else {
                if ((unit.equals("MINUTES") && time.toInt() < 15) || time.toInt() == 0) {
                    Toast.makeText(baseContext ,"The Minimum Allowed Time is 15 Minutes", Toast.LENGTH_LONG)
                        .show();
                } else {

                    val editor = sharedPref.edit()
                    editor.putInt("Time", time.toInt())
                    editor.putString("Unit", unit)
                    editor.apply()
                    setWorkManager()
                    Log.i("tag", "OK")
                    finish()
                }
            }
        }
    }
    private fun setWorkManager() {

        var time = sharedPref?.getInt("Time", 15)?.toLong()
        var unit = sharedPref?.getString("Unit", "MINUTES").toString()
        //  var timeunit = TimeUnit.valueOf(unit)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        var test = TimeUnit.MINUTES

        if (unit.equals("MINUTES")) {

            test = TimeUnit.MINUTES

        } else if (unit.equals("DAYS")) {

            test = TimeUnit.DAYS

        } else if (unit.equals("HOURS")) {

            test = TimeUnit.HOURS

        }

        Log.i("tag","WORK MANAGER")
        Log.i("tag","" + time + "")
        val workerInstance = PeriodicWorkRequest.Builder(
            UploadWorker::class.java, time!!, test
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workerInstance)

    }
}
