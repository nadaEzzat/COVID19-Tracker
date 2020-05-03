package ITI.covid19_tracker

import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.WorkManager.MyReceiver
import ITI.covid19_tracker.WorkManager.UploadWorker
import ITI.covid19_tracker.db.FetchingAPIData.FetchData
import ITI.covid19_tracker.db.FetchingAPIData.StatisticData.FetchStatisticData
import ITI.covid19_tracker.view.Tab.ActivityTab
import ITI.covid19_tracker.viewmodel.MainViewModel
import ITI.covid19_tracker.viewmodel.StatisticViewModel
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*
import java.util.concurrent.TimeUnit


class SplashScreen : AppCompatActivity() {

    var sharedPref: SharedPreferences? = null
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "work-manager"
    private val SPLASH_TIME_OUT: Long = 3000 // 1 sec

    var staticViewModel: StatisticViewModel? = null

    lateinit var fetchStatisticData: FetchStatisticData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fetchStatisticData = FetchStatisticData()
        mContext = applicationContext
        ViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        staticViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)
        sharedPref = applicationContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        setWorkManager()
        FetchStatisticAPIData()

        val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        linearId.startAnimation(animation)
        Handler().postDelayed({
            startActivity(Intent(this, ActivityTab::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }


    private fun AlarmManager() {
        val alarmManager =
            mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //   AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        var intent: Intent = Intent(this, MyReceiver::class.java)


        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 35)


        //     Intent intent = new Intent(this, MyReceiver.class);
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
/*
Alarm will be triggered approximately after one hour and will be repeated every hour after that
*/
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            60000,
            pendingIntent
        )

        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);

    }

    private fun setWorkManager() {

        var time = sharedPref?.getInt("Time", 15)?.toLong()
        var unit = sharedPref?.getString("Unit", "MINUTES").toString()

        var test = TimeUnit.MINUTES
        if (unit.equals("MINUTES")) {

            test = TimeUnit.MINUTES

        } else if (unit.equals("DAYS")) {

            test = TimeUnit.DAYS

        } else if (unit.equals("HOURS")) {

            test = TimeUnit.HOURS

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName: String = mContext.getPackageName()
            val pm =
                mContext.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {

                Log.i("tag", "WORK MANAGERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
                Log.i("tag", "" + time + "")

/*
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workerInstance = PeriodicWorkRequest.Builder(
            UploadWorker::class.java, time!!, test
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workerInstance)
*/

                val pwr = PeriodicWorkRequest.Builder(
                    UploadWorker::class.java, time!!, test
                )
                    .setConstraints(Constraints.NONE)
                    .build()

                WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                    "my_worker",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    pwr
                )


            }
        }


    }

    companion object {
        var fetchData = FetchData()
        lateinit var mContext: Context
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
            }
        }

        fun checkInternetConnection(): Boolean {
            return checkNetworkConnection.hasInternetConnection(mContext)
        }
    }

    fun FetchStatisticAPIData() {
        Log.i("tag", "Fetching Static data")
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
