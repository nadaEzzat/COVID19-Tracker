package ITI.covid19_tracker.db.FetchingAPIData.StatisticData

import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.SplashScreen
import ITI.covid19_tracker.db.FetchingAPIData.APIInterface
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.Model
import ITI.covid19_tracker.model.st_Model
import ITI.covid19_tracker.model.statisticModel
import ITI.covid19_tracker.view.MainActivity
import ITI.covid19_tracker.viewmodel.MainViewModel
import ITI.covid19_tracker.viewmodel.StatisticViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.net.Network
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class FetchStatisticData () : AppCompatActivity() {//, FetchCompleteListener {

   //lateinit var context : Context
    private lateinit var CountryDetailsJSON: JSONObject
    var checkNetworkConnection = newtwork()
    private var ViewModel: StatisticViewModel? = null

    fun checkInternetConnection(): Boolean {
        return checkNetworkConnection.hasInternetConnection(SplashScreen.mContext)
    }

    @SuppressLint("CheckResult")
    fun getDetails(viewmodel: StatisticViewModel?) {
        if (checkInternetConnection()) {

            ViewModel = viewmodel
            CountryDetailsJSON = JSONObject()
            CountryDetailsJSON.put("valid", false)

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

            val D: APIStatistic = retrofit.create(APIStatistic::class.java)

            var call: Observable<st_Model> = D.getAllData()
            call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response -> onResponse(response)}, {t -> onFailure(t) })

        }
    }

    private fun onFailure(t: Throwable?) {
        Log.i("tag", "Activity Failure $t")
        println("Activity Failure.")
    }

    private fun onResponse(response: st_Model?) {
        // Delete previuos data
        //ViewModel?.delete()
        val Response = response!!
        // var count = Response.countries_stat?.size
        var new_cases =
            Response.new_cases
        var total_cases = Response.total_cases
        var new_deaths =
            Response.new_deaths
        var total_deaths =
            Response.total_deaths
        var statistic_taken_at = Response.statistic_taken_at
        var total_recovered =
            Response.total_recovered
        val statisticModel = statisticModel(
            // 0,
            new_cases,
            new_deaths,
            total_recovered,
            total_deaths,
            total_cases,
            statistic_taken_at
        )
        // check if this country is subscribed or not
        Log.i("tag", "TESSSSSSSSSSSSSSSSSt StattTTTTTTistic  " + statisticModel.new_cases + "  " + statistic_taken_at)

        ViewModel?.setCountry(statisticModel)

    }
}

