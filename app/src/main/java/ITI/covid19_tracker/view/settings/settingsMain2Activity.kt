package ITI.covid19_tracker.view.settings

import ITI.covid19_tracker.R
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings_main2.*

class settingsMain2Activity : AppCompatActivity() {
    companion object{
        private var PRIVATE_MODE = 0
        private val PREF_NAME = "work-manager"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_main2)
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
                    Log.i("tag", "OK")
                    finish()
                }
            }
        }
    }
}
