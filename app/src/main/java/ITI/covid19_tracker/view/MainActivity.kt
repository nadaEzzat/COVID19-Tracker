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


    lateinit var searchBtn: ImageButton
    lateinit var searchBox: TextView
    lateinit var SwipeRefresh: SwipeRefreshLayout
    lateinit var countryRecyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.activity_main, container, false)



        setUoComponents(view)

        //Get API Data
        //  FetchDataAPIData()

        //WorkManager
        //setWorkManager()

        getDataFomDataBase()

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

                Toast.makeText(requireContext(), "Please Enter Countery Name", Toast.LENGTH_LONG).show()

            } else {

                ViewModel?.search(country)?.observe(this, Observer<List<Country>> { this.renderSearch(it) })

            }
        }

        // pull-to-refresh
        SwipeRefresh.setOnRefreshListener {
            FetchDataAPIData()
            SwipeRefresh.setRefreshing(false);
            Toast.makeText(mcontext, "Loading Data...", Toast.LENGTH_SHORT).show();

        }
        return view
    }

    private fun setUoComponents(view: View) {
        searchBtn = view.findViewById(R.id.searchButton)
        searchBox = view.findViewById(R.id.searchBox)
        SwipeRefresh = view.findViewById(R.id.SwipeRefresh)
        countryRecyclerView = view.findViewById(R.id.countryRecyclerView)

        ViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        sharedPref = mcontext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

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
        var ViewModel: MainViewModel? = null
        var fetchData = FetchData()
        val checkNetworkConnection = newtwork()

        fun FetchDataAPIData() {

            if (checkInternetConnection()) {

                fetchData.getDetails(ViewModel)

            } else {
                Toast.makeText(mcontext, "Please Connect to the Internet to Get Latest Data", Toast.LENGTH_LONG).show();
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
