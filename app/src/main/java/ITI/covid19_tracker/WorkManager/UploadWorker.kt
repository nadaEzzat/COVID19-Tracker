package ITI.covid19_tracker.WorkManager

import ITI.covid19_tracker.SplashScreen
import ITI.covid19_tracker.view.MainActivity
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        println("Work Done")
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)
        println(formatedDate)
        SplashScreen.FetchDataAPIData()
        return Result.success()

    }
}