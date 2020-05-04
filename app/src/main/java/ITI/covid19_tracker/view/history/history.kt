package ITI.covid19_tracker.view.history

import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.R
import ITI.covid19_tracker.db.historyAPI.HistoryInterface
import ITI.covid19_tracker.model.HistoryModel
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class history : AppCompatActivity() {

    var month: Int = 5
    lateinit var context: Context
    val checkNetworkConnection = newtwork()
    lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        barChart = findViewById(R.id.history_chart_id)

        supportActionBar!!.title = "History by particular country"

        context = applicationContext

        Toast.makeText(context, "Loading Data...", Toast.LENGTH_SHORT)
            .show();


        var country_name : String = intent.getStringExtra("country_name")
        getHistory(country_name)


    }


    fun checkInternetConnection(): Boolean {
        return checkNetworkConnection.hasInternetConnection(application)
    }

    @SuppressLint("CheckResult")
    fun getHistory(country_name : String ) {

        val params: MutableMap<String, String> = HashMap()
        params["country"] = country_name

        if (checkInternetConnection()) {

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response? {
                    val original: Request = chain.request()
                    val request: Request = original.newBuilder()
                        .header(
                            "x-rapidapi-key",
                            "a3e81e7013mshcdca914a7a0f3f5p1173edjsn44fd70a93af2"
                        )
                        .header("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com")
                        .method(original.method(), original.body())
                        .build()
                    return chain.proceed(request)
                }
            })

            val client = httpClient.build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://coronavirus-monitor.p.rapidapi.com/coronavirus/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()


            val D: HistoryInterface = retrofit.create(HistoryInterface::class.java)
            var call = D.getHistoryData(params)
            call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                // .filter { json -> check(json.record_date) }
                .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })

        } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG)
                .show();
        }


    }

    private fun onFailure(t: Throwable?) {
        Log.i("tag", "Activity Failure $t")

        println("Activity Failure.")
    }

    private fun onResponse(response: HistoryModel) {

        var moths: ArrayList<String> = arrayListOf()
        var cases: ArrayList<String> = arrayListOf()

        println("ResponseData")
        val Response = response!!

        if (response.stat_by_country.isNullOrEmpty()) {
        } else {
            println(response.stat_by_country!!.size!!)
            for (i in 0..response.stat_by_country!!.size!! - 1) {
                var date = response.stat_by_country!![i].record_date
                var s = date.split("-")
                if (s[1].equals("0" + month)) {
                    moths.add(month.toString())
                    var newDate = response.stat_by_country!![i].total_cases.replace(",", ",")
                    cases.add(response.stat_by_country!![i].total_cases.toString())
                    month--
                    println(date)
                }
            }
            DarwBarChart(moths, cases , response.country)

        }


    }


    private fun DarwBarChart(m: ArrayList<String>, cases: ArrayList<String> , country : String) {

        val colors: ArrayList<Int> = ArrayList()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())


        var count = cases.size
        var barEntities = ArrayList<BarEntry>()

        if (cases != null) {
            if (cases.size > 0) {

                for (i in 0..cases.size - 1) {
                    println(
                        cases?.get(i).replace(
                            ",",
                            "",
                            false
                        ).toFloat()!!
                    )
                    barEntities.add(
                        BarEntry(
                            cases?.get(i).replace(
                                ",",
                                "",
                                false
                            ).toFloat()!!, i
                        )
                    )
                }

                var dataSet: BarDataSet = BarDataSet(barEntities, "Total Cases")
                //dataSet.barSpacePercent = 1f
                dataSet.setColors(colors)
                //                dataSet.setColor(getResources().getColor(R.color.Red)); //resolved color
                barChart.setDescriptionTextSize(11f);
                barChart.setDescription("History of "+country)  // set the description

                val arr = mutableListOf<String>()
                for (i in 0..cases.size - 1) {
                    arr.add(m[i])
                }

                var bar: BarData = BarData(arr, dataSet)
                barChart.data = bar


                barChart.setDescriptionTextSize(22f)


                barChart.invalidate()

            } else {
                Log.i("tag", "Count < 0")
            }
        } else {
            Log.i("tag", "NUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUULLLLLLLL = null")
        }
    }
}
