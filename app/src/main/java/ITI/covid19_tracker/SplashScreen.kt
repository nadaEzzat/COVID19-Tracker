package ITI.covid19_tracker

import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.WorkManager.UploadWorker
import ITI.covid19_tracker.db.FetchingAPIData.FetchData
import ITI.covid19_tracker.db.FetchingAPIData.StatisticData.FetchStatisticData
import ITI.covid19_tracker.view.MainActivity
import ITI.covid19_tracker.view.Tab.ActivityTab
import ITI.covid19_tracker.viewmodel.MainViewModel
import ITI.covid19_tracker.viewmodel.StatisticViewModel
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.concurrent.TimeUnit

class SplashScreen : AppCompatActivity()  {

    var sharedPref: SharedPreferences? = null
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "work-manager"

    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec

    var staticViewModel: StatisticViewModel? = null
    lateinit var fetchStatisticData: FetchStatisticData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fetchStatisticData = FetchStatisticData()
        mContext = applicationContext
        ViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        staticViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)

        sharedPref=  applicationContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        setWorkManager()
        FetchStaticAPIData()

        val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        linearId.startAnimation(animation)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, ActivityTab::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
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


        val workerInstance = PeriodicWorkRequest.Builder(
            UploadWorker::class.java, time!!, test
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workerInstance)

    }

    companion object {
        var fetchData = FetchData()
        lateinit var mContext : Context
        var ViewModel: MainViewModel? = null
        val checkNetworkConnection = newtwork()

        fun FetchDataAPIData() {
            Log.i("tag", "Fetching data")

            if (checkInternetConnection()) {
                fetchData.getDetails(ViewModel)
            } else {
                Toast.makeText(
                    mContext,
                    "Please Connect to the Internet to Get Latest Data",
                    Toast.LENGTH_LONG
                )
                    .show();
                Log.i("tag", "Please Check Your Internet Connection to Get Latest Data")
            }
        }
        fun checkInternetConnection(): Boolean {
            return checkNetworkConnection.hasInternetConnection(mContext)
        }
    }
    fun FetchStaticAPIData() {
        Log.i("tag", "Fetching data")
        if (checkInternetConnection()) {
            fetchStatisticData.getDetails(staticViewModel)
        } else {
            Toast.makeText(
                mContext,
                "Please Connect to the Internet to Get Latest Data",
                Toast.LENGTH_LONG
            )
                .show();
            Log.i("tag", "Please Check Your Internet Connection to Get Latest Data")
        }
    }

}
