package ITI.covid19_tracker.view

import ITI.covid19_tracker.FetchingAPIData.FetchData
import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.NotificationHelper
import ITI.covid19_tracker.R
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Fragment() {

    val checkNetworkConnection = newtwork()

    lateinit var adapter: MainAdapter
    var ViewModel: MainViewModel? = null
    var fetchData = FetchData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.activity_main, container, false)

        var searchBtn: ImageButton = view.findViewById(R.id.searchButton)
        var searchBox: TextView = view.findViewById(R.id.searchBox);
        var SwipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.SwipeRefresh)
        var countryRecyclerView: RecyclerView = view.findViewById(R.id.countryRecyclerView)


        ViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        //Get API Data
       FetchDataAPIData()

        //WorkManager
         setWorkManager()

        getDataFomDataBase()
        // messageViewModel?.getMessages()?.observe(this, Observer<List<Country>> { this.renderMessges(it) })

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().trim { it <= ' ' }.isEmpty()) {
                    getDataFomDataBase()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        // Search
        searchBtn.setOnClickListener {
            var country: String = searchBox.text.toString()
            if (country.trim().equals("")) {
                Toast.makeText(requireContext(), "Please Enter Countery Name", Toast.LENGTH_LONG)
                    .show()
            } else {
                Log.i("tag", "SearchButton")
                ViewModel?.search(country)
                    ?.observe(this, Observer<List<Country>> { this.renderSearch(it) })
            }
        }

        // pull-to-refresh
        SwipeRefresh.setOnRefreshListener {
            Log.i("tag", "FirstSCROLLING")
            FetchDataAPIData()
            SwipeRefresh.setRefreshing(false);
            val snack = Snackbar.make(ParentLayout, "Your Data is Updated", Snackbar.LENGTH_LONG)
            snack.show()


        }
        return view
    }


    //check network connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView(R.layout.activity_main)

    }

    private fun getDataFomDataBase() {
        ViewModel?.getAllData()
            ?.observe(this, Observer<List<Country>> { this.getData(it) })
    }

    private fun setWorkManager() {
/*
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workerInstance = PeriodicWorkRequest.Builder(
            UploadWorker::class.java, 1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(workerInstance)
  */
        val notificationHelper =
            NotificationHelper(context, "HI NADA ", "wait your trip")
        val nb: NotificationCompat.Builder? =
            notificationHelper.getChannelNotification()

        val notifyIntent: Intent = Intent()
        notifyIntent.setClassName(
            "com.ProjectITI.tripsproject",
            "com.ProjectITI.tripsproject.ShowAlertDialog"
        )
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val notifyPendingIntent = PendingIntent.getActivity(
            context,
            notifyIntent.getIntExtra("request", 0),
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (nb != null) {
            nb.setContentIntent(notifyPendingIntent)
        }
        notificationHelper.getManager().notify(Intent().getIntExtra("request", 0), nb?.build())
    }

    private fun getData(country: List<Country>?) {

        if (country.isNullOrEmpty()) {

        } else {
            setupRecycleData(country)
        }
    }

    private fun renderSearch(country: List<Country>?) {

        if (country.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "This country is not Valid", Toast.LENGTH_LONG)
                .show();
        } else {
            setupRecycleData(country)
        }
    }

    fun setupRecycleData(country: List<Country>?) {
        adapter = MainAdapter(requireContext(), country,ViewModel)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = false
        countryRecyclerView.layoutManager = layoutManager
        countryRecyclerView.adapter = adapter
    }

    fun FetchDataAPIData() {
        Log.i("tag", "Fetching data")
        if (checkInternetConnection()) {
            fetchData.getDetails(ViewModel)
        } else {
            Toast.makeText(
                requireContext(),
                "Please Connect to the Internet to Get Latest Data",
                Toast.LENGTH_LONG
            )
                .show();
            Log.i("tag", "Please Check Your Internet Connection to Get Latest Data")
        }
    }

    fun checkInternetConnection(): Boolean {
        return checkNetworkConnection.hasInternetConnection(requireContext())
    }
}
