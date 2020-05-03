package ITI.covid19_tracker.db.FetchingAPIData

import ITI.covid19_tracker.Network.newtwork
import ITI.covid19_tracker.SplashScreen
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.Model
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class FetchData() : AppCompatActivity() {

    private lateinit var CountryDetailsJSON: JSONObject

    private var ViewModel: MainViewModel? = null

    val checkNetworkConnection = newtwork()

    fun checkInternetConnection(): Boolean {
        return checkNetworkConnection.hasInternetConnection(SplashScreen.mContext)
    }

    @SuppressLint("CheckResult")
    fun getDetails(viewmodel: MainViewModel?) {

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

            val D: APIInterface = retrofit.create(APIInterface::class.java)
            var call: Observable<Model> = D.getAllData()
            call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response -> onResponse(response)}, {t -> onFailure(t) })


             /*
            call.enqueue(object : Callback<Model> {
                override fun onResponse(call: Call<Model>, response: retrofit2.Response<Model>) {
                    Log.i("tag", "test2")
                    if (response.code() == 200) {

                        // Delete previuos data
                        //ViewModel?.delete()
                        val arr: ArrayList<Country> = ArrayList<Country>()
                        val Response = response.body()!!

                        var count = Response.countries_stat?.size
                        Log.i("tag", " Count : $count")
                        if (count != null) {
                            for (i in 0..(count - 1)) {
                                var country_name =
                                    Response.countries_stat?.get(i)!!.country_name
                                var cases = Response.countries_stat?.get(i)!!.cases
                                var new_cases = Response.countries_stat?.get(i)!!.new_cases
                                var total_recovered =
                                    Response.countries_stat?.get(i)!!.total_recovered
                                var deaths = Response.countries_stat?.get(i)!!.deaths
                                var new_deaths = Response.countries_stat?.get(i)!!.new_deaths
                                var total_cases_per_1m_population =
                                    Response.countries_stat?.get(i)!!.total_cases_per_1m_population
                                val country = Country(
                                    // 0,
                                    country_name,
                                    cases,
                                    new_cases,
                                    total_recovered,
                                    deaths,
                                    new_deaths,
                                    total_cases_per_1m_population,
                                    "0"
                                )

                                arr.add(country)
                                // check if this country is subscribed or not
                                // Log.i("tag", "check if this " + country.country_name + "  " + ViewModel?.checkSubscribtion(country)!!)

                                //    ViewModel?.setCountry(country)
                            }
                            ViewModel?.setALLCountry(arr)
                        }


                    }
                    call.cancel()
                }

                override fun onFailure(call: Call<Model>, t: Throwable) {
                    Log.i("tag", "Activity Failure $t")
                    println("Activity Failure.")
                    call.request()
                }

            })
            */

        }
    }
    fun onFailure(t: Throwable) {
        Log.i("tag", "Activity Failure $t")
        println("Activity Failure.")

    }
    private fun onResponse(response: Model?) {

        val arr: ArrayList<Country> = ArrayList<Country>()
            val Response = response!! //response.body()!!

            var count = Response.countries_stat?.size
            Log.i("tag", " Count : $count")
            if (count != null) {
                for (i in 0..(count - 1)) {
                    var country_name =
                        Response.countries_stat?.get(i)!!.country_name
                    var cases = Response.countries_stat?.get(i)!!.cases
                    var new_cases = Response.countries_stat?.get(i)!!.new_cases
                    var total_recovered =
                        Response.countries_stat?.get(i)!!.total_recovered
                    var deaths = Response.countries_stat?.get(i)!!.deaths
                    var new_deaths = Response.countries_stat?.get(i)!!.new_deaths
                    var total_cases_per_1m_population =
                        Response.countries_stat?.get(i)!!.total_cases_per_1m_population
                    val country = Country(
                        // 0,
                        country_name,
                        cases,
                        new_cases,
                        total_recovered,
                        deaths,
                        new_deaths,
                        total_cases_per_1m_population,
                        "0"
                    )

                    arr.add(country)

                }
                //Save Data
                ViewModel?.setALLCountry(arr)
            }

    }

}
