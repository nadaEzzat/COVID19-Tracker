package ITI.covid19_tracker.view

import ITI.covid19_tracker.db.FetchingAPIData.FetchData
import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.R
import ITI.covid19_tracker.WorkManager.UploadWorker
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.content.Context
import android.content.SharedPreferences
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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : Fragment() {

    lateinit var adapter: MainAdapter
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "work-manager"
    var sharedPref: SharedPreferences? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPref = mcontext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.activity_main, container, false)
        var searchBtn: ImageButton = view.findViewById(R.id.searchButton)
        var searchBox: TextView = view.findViewById(R.id.searchBox)
        var SwipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.SwipeRefresh)
        var countryRecyclerView: RecyclerView = view.findViewById(R.id.countryRecyclerView)

        ViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        //Get API Data
        //  FetchDataAPIData()

        //WorkManager
        //setWorkManager()

        getDataFomDataBase()
        // ViewModel?.getMessages()?.observe(this, Observer<List<Country>> { this.renderMessges(it) })

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
            // setWorkManager()

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
            Toast.makeText(
                mcontext,
                "Loading Data...",
                Toast.LENGTH_SHORT
            )
                .show();

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
            ?.observe(this, Observer<List<Country>> { this.setData(it) })
    }

    private fun setData(country: List<Country>?) {

        if (country.isNullOrEmpty()) {

            LayoutRecycle.visibility = View.INVISIBLE
            NoData.visibility = View.VISIBLE

        } else {
            NoData.visibility = View.INVISIBLE
            LayoutRecycle.visibility = View.VISIBLE
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
        adapter = MainAdapter(requireContext(), country, ViewModel)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = false
        countryRecyclerView.layoutManager = layoutManager
        countryRecyclerView.adapter = adapter
    }

    companion object {
        lateinit var mcontext: Context
        // lateinit var sharedPref: SharedPreferences
        var ViewModel: MainViewModel? = null
        var fetchData = FetchData()
        val checkNetworkConnection = newtwork()
        fun FetchDataAPIData() {
            Log.i("tag", "Fetching data")
            if (checkInternetConnection()) {
                fetchData.getDetails(ViewModel)
            } else {
                Toast.makeText(
                    mcontext,
                    "Please Connect to the Internet to Get Latest Data",
                    Toast.LENGTH_LONG
                )
                    .show();
                Log.i("tag", "Please Check Your Internet Connection to Get Latest Data")
            }
        }

        fun checkInternetConnection(): Boolean {
            return checkNetworkConnection.hasInternetConnection(mcontext)
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


        val workerInstance = PeriodicWorkRequest.Builder(
            UploadWorker::class.java, time!!, test
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(workerInstance)
    }



}
