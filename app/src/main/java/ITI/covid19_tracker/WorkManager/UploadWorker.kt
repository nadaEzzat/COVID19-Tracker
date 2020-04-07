package ITI.covid19_tracker.WorkManager

import ITI.covid19_tracker.FetchingAPIData.FetchData
import ITI.covid19_tracker.MainActivity
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {




    override fun doWork(): Result {


        println("Work Done")
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)
        println(formatedDate)
      //  MainActivity.fetchData.getDetails()
        return Result.success()

//        // Get the input
//        val imageUriInput = getInputData().getString(Constants.KEY_IMAGE_URI)
//        // TODO: validate inputs.
//        // Do the work
//        val response = uploadFile(imageUriInput)
//
//        // Create the output of the work
//        val outputData = workDataOf(Constants.KEY_IMAGE_URL to response.imageUrl)

        // Return the output
        // return Result.success(outputData)

    }
}