package ITI.covid19_tracker

import ITI.covid19_tracker.FetchingAPIData.FetchData
import ITI.covid19_tracker.WorkManager.UploadWorker
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {

    }

    lateinit var adapter:MainAdapter
    var messageViewModel: MainViewModel? = null
    var fetchData = FetchData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messageViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

       // setWorkManager()

        //Get API Data
        fetchData.getDetails(messageViewModel)

        getDataFomDataBase()
       // messageViewModel?.getMessages()?.observe(this, Observer<List<Country>> { this.renderMessges(it) })

        // Enable Send button when there's text to send
        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                searchButton.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
                if(charSequence.toString().trim { it <= ' ' }.isEmpty())
                {
                    getDataFomDataBase()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        messageEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(100))

        // Send button sends a message and clears the EditText

        searchButton.setOnClickListener {
            Log.i("tag","SearchButton")
            var country:String = messageEditText.text.toString()

            messageViewModel?.search(country)?.observe(this, Observer<List<Country>> { this.renderMessges(it) })

            // messageViewModel?.search(messageEditText.text.toString())
            /*
                       val message = Country(0,"Cairo" , "454545","5454", "454545","5454", "454545")


                       // Clear input box
                       messageEditText.setText("")

                        */
        }

    }

    private fun getDataFomDataBase() {
        messageViewModel?.getMessages()?.observe(this, Observer<List<Country>> { this.renderMessges(it) })
    }

    private fun setWorkManager() {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(UploadWorker::class.java, 20, TimeUnit.SECONDS).build()
      //  val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>() .build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
        val mWorkManager = WorkManager.getInstance(applicationContext)
    }

    private fun renderMessges(messages: List<Country>?){
        print("RenderMessage")
        adapter = MainAdapter(this,messages)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = false
        messageRecyclerView.layoutManager = layoutManager
        messageRecyclerView.adapter = adapter
    }


}






//  var fetchData =  FetchData()
//fetchData.getDetails()


/*
Log.i("tag" , "Fijijijijijij")
Thread {
    //Do your database´s operations here

val db = Room.databaseBuilder(
    applicationContext,
    CountryDatabase::class.java, "country_database.db"
).build()
db.countryDao().insert(Country(1,"Cairo","4500"))
var data : LiveData<List<Country>> = db.countryDao().getAll()

Log.i("tag" , "Testing ")
Log.i("tag" , data.value.toString())
Log.i("tag" , data.value?.get(0).toString())
   // println(it)
}.start()

 */
/*
var constraints = Constraints.Builder()
    .setRequiresBatteryNotLow(true)
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresCharging(true)
    .setRequiresStorageNotLow(true)
    .build();
*/
/*
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(UploadWorker::class.java, 20, TimeUnit.SECONDS).build()

        WorkManager.getInstance().enqueue(periodicWorkRequest)
*/