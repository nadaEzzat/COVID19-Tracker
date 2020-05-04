package ITI.covid19_tracker.view.statistics

import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.R
import ITI.covid19_tracker.db.FetchingAPIData.StatisticData.FetchStatisticData
import ITI.covid19_tracker.model.statisticModel
import ITI.covid19_tracker.viewmodel.StatisticViewModel
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate


class statisticsFragment : Fragment() {


    lateinit var Static_context: Context
    lateinit var timeOftatistic: TextView
    lateinit var barChart: PieChart
    var ViewModel: StatisticViewModel? = null
    lateinit var fetchStatisticData: FetchStatisticData

    val checkNetworkConnection = newtwork()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_statistics, container, false)

        var SwipeRefreshBarChart: SwipeRefreshLayout = view.findViewById(R.id.SwipeRefreshBarChart)

        Static_context = this!!.context!!
        timeOftatistic = view.findViewById(R.id.timeOftatistic)
        fetchStatisticData = FetchStatisticData()
        ViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)

        //   FetchStaticAPIData()
        ViewModel?.getAllData()?.observe(this, Observer<List<statisticModel>> { this.setData(it) })

        barChart = view.findViewById(R.id.BarChartId)


        SwipeRefreshBarChart.setOnRefreshListener {

            FetchStaticAPIData()
            SwipeRefreshBarChart.setRefreshing(false);


        }
        return view
    }

    private fun setData(it: List<statisticModel>?) {

        val colors: ArrayList<Int> = ArrayList()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())


        var count = it?.size
        var barEntities = mutableListOf<Entry>()

        if (it != null) {
            if (it.size > 0) {

                barEntities.add(
                    BarEntry(
                        it?.get(0)?.new_cases?.replace(",", "", false).toFloat()!!,
                        0
                    )
                )

                barEntities.add(
                    BarEntry(
                        it?.get(0)?.total_cases?.replace(
                            ",",
                            "",
                            false
                        ).toFloat()!!, 2
                    )
                )
                barEntities.add(
                    BarEntry(
                        it?.get(0)?.new_deaths?.replace(
                            ",",
                            "",
                            false
                        ).toFloat()!!, 1
                    )
                )
                barEntities.add(
                    BarEntry(
                        it?.get(0)?.total_deaths?.replace(
                            ",",
                            "",
                            false
                        ).toFloat()!!, 3
                    )
                )
                barEntities.add(
                    BarEntry(
                        it?.get(0)?.total_recovered?.replace(
                            ",",
                            "",
                            false
                        ).toFloat()!!, 4
                    )
                )

                var dataSet: PieDataSet = PieDataSet(barEntities, "Number")
                //dataSet.barSpacePercent = 1f
                dataSet.setColors(colors)

                val arr = mutableListOf<String>()
                arr.add("New Cases")
                arr.add("Total Cases")
                arr.add("New Death")
                arr.add("Total Deaths")
                arr.add("Total Recoverd")

                var bar: PieData = PieData(arr, dataSet)
                // bar.groupSpace = 1f
                barChart.data = bar
                //   barChart.defaultValueFormatter(PercentFormatter(chart));
                barChart.setCenterTextSize(11f);
                barChart.setCenterTextColor(Color.WHITE);

                // undo all highlights
                barChart.highlightValues(null);
                barChart.setTouchEnabled(true)
                barChart.setBackgroundColor(1)
                //   barChart.setOnTouchListener { v, event ->  }
                //  barChart.setDescription("World Total Stat")
                barChart.setDescriptionTextSize(22f)


                barChart.invalidate()
                timeOftatistic.setText(it?.get(0)?.statistic_taken_at)
            } else {
                Log.i("tag", "Count < 0")
            }
        } else {
            Log.i("tag", "NUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUULLLLLLLL = null")
        }
    }

    fun FetchStaticAPIData() {
        Log.i("tag", "Fetching data")
        if (checkInternetConnection()) {
            fetchStatisticData.getDetails(ViewModel)

            Toast.makeText(
                Static_context,
                "Loading Data...",
                Toast.LENGTH_SHORT
            )
                .show();
            barChart.invalidate()

        } else {
            Toast.makeText(
                Static_context,
                "Please Connect to the Internet to Get Latest Data",
                Toast.LENGTH_LONG
            )
                .show();
            Log.i("tag", "Please Check Your Internet Connection to Get Latest Data")
        }
    }

    fun checkInternetConnection(): Boolean {
        return checkNetworkConnection.hasInternetConnection(Static_context)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Static_context = context
    }

}



