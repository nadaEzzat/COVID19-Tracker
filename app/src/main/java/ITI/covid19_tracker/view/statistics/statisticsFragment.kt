package ITI.covid19_tracker.view.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ITI.covid19_tracker.R
import ITI.covid19_tracker.db.FetchingAPIData.StatisticData.FetchStatisticData
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.statisticModel
import ITI.covid19_tracker.view.MainActivity
import ITI.covid19_tracker.viewmodel.MainViewModel
import ITI.covid19_tracker.viewmodel.StatisticViewModel
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class statisticsFragment : Fragment() {

    lateinit var timeOftatistic : TextView
    lateinit var barChart: BarChart
    var ViewModel: StatisticViewModel? = null
    lateinit var fetchStatisticData: FetchStatisticData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_statistics, container, false)

        timeOftatistic  = view.findViewById(R.id.timeOftatistic)
        fetchStatisticData = FetchStatisticData()
        ViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)

     //   FetchStaticAPIData()
        ViewModel?.getAllData()?.observe(this, Observer<List<statisticModel>> { this.setData(it) })

        barChart = view.findViewById(R.id.BarChartId)



        return view
    }

    private fun setData(it: List<statisticModel>?) {

        var count = it?.size
        var barEntities: ArrayList<BarEntry> = ArrayList<BarEntry>()

        if (it != null) {
            if(it.size>0) {

                barEntities.add(BarEntry(it?.get(0)?.new_cases?.replace(",","",false).toFloat()!!, 0))
                barEntities.add(BarEntry(it?.get(0)?.new_deaths?.replace(",","",false).toFloat()!!, 1))
                barEntities.add(BarEntry(it?.get(0)?.total_cases?.replace(",","",false).toFloat()!!, 2))
                barEntities.add(BarEntry(it?.get(0)?.total_deaths?.replace(",","",false).toFloat()!!, 3))
                barEntities.add(BarEntry(it?.get(0)?.total_recovered?.replace(",","",false).toFloat()!!, 4))

                var barSet: BarDataSet = BarDataSet(barEntities, "Number")
                barSet.barSpacePercent=1f
                val arr = ArrayList<String>()
                arr.add("New Cases")
                arr.add("New Death")
                arr.add("Total Cases")
                arr.add("Toltal Deaths")
                arr.add("Total Recoverd")

                var bar: BarData = BarData(arr, barSet)
                bar.groupSpace=1f
                barChart.data = bar
                barChart.setTouchEnabled(true)
             //   barChart.setOnTouchListener { v, event ->  }
                barChart.isDragEnabled = true
                barChart.setDescription("World Total Stat")

barChart.invalidate()
                timeOftatistic.setText( it?.get(0)?.statistic_taken_at)
            }
            Log.i("tag","ERRRRRRRRRRRRRRRRRRRRRRRRRROR ")
        }
        Log.i("tag","ERRRRRRRRRRRRRRRRRRRRRRRRRROR RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
    }

    /*fun FetchStaticAPIData() {
        Log.i("tag", "Fetching data")
        if (MainActivity.checkInternetConnection()) {
            fetchStatisticData.getDetails(ViewModel)
        } else {
            Toast.makeText(
                MainActivity.mcontext,
                "Please Connect to the Internet to Get Latest Data",
                Toast.LENGTH_LONG
            )
                .show();
            Log.i("tag", "Please Check Your Internet Connection to Get Latest Data")
        }
    }*/

}



