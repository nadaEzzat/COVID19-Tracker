package ITI.covid19_tracker.db.FetchingAPIData

import ITI.covid19_tracker.SplashScreen
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.Model
import ITI.covid19_tracker.view.MainActivity
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class FetchData() : AppCompatActivity(){//, FetchCompleteListener {

    private lateinit var CountryDetailsJSON: JSONObject

    private var ViewModel: MainViewModel? = null

    fun checkInternetConnection(): Boolean {
        return SplashScreen.checkNetworkConnection.hasInternetConnection(SplashScreen.mContext)
    }

    fun getDetails(viewmodel: MainViewModel? ) {
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
                .client(client)
                .build()

            val D: APIInterface = retrofit.create(APIInterface::class.java)
            var call: Call<Model> = D.getAllData()
            Log.i("tag", "test1")
            call.enqueue(object : Callback<Model> {
                override fun onResponse(call: Call<Model>, response: retrofit2.Response<Model>) {
                    Log.i("tag", "test2")
                    if (response.code() == 200) {

                        // Delete previuos data
                        //ViewModel?.delete()
                        val arr : ArrayList<Country> = ArrayList<Country>()
                        val weatherResponse = response.body()!!

                        var count = weatherResponse.countries_stat?.size
                        Log.i("tag", " Count : $count")
                        if (count != null) {
                            for (i in 0..(count - 1)) {
                                var country_name =
                                    weatherResponse.countries_stat?.get(i)!!.country_name
                                var cases = weatherResponse.countries_stat?.get(i)!!.cases
                                var new_cases = weatherResponse.countries_stat?.get(i)!!.new_cases
                                var total_recovered =
                                    weatherResponse.countries_stat?.get(i)!!.total_recovered
                                var deaths = weatherResponse.countries_stat?.get(i)!!.deaths
                                var new_deaths = weatherResponse.countries_stat?.get(i)!!.new_deaths
                                var total_cases_per_1m_population =
                                    weatherResponse.countries_stat?.get(i)!!.total_cases_per_1m_population
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
            Log.i("tag", "test3")
        }
    }
    /*
    fun getDetails(message: MainViewModel? ) {
        ViewModel = message

        Log.i("tag","getDehhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhtails")
        CountryDetailsJSON = JSONObject()
        CountryDetailsJSON.put("valid", false)
        var client = OkHttpClient()
        var request = OkHttpRequest(client)
        val url = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/cases_by_country.php"

        request.GET(url, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                val responseData = response.body()?.string()
                runOnUiThread {
                    try {
                        var json = JSONObject(responseData)
                        Log.i("tag", "SUCCESS")
                        println("SUCCESS - " + json)
                        CountryDetailsJSON = json
                        CountryDetailsJSON.put("valid", true)
                        this@FetchData.fetchComplete()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Log.i("tag", "Activity Failure")

                println("Activity Failure.")
            }
        })
    }

    override fun fetchComplete() {
        println("fetchCOmplete:   " + CountryDetailsJSON)
        Log.i("tag", "fetchCOmplete")

        if (CountryDetailsJSON.getBoolean("valid")) {

            Log.i("tag", "If Json is Valid")
            // Delete previuos data
            ViewModel?.delete()
            var arr = CountryDetailsJSON.getJSONArray("countries_stat")
            for (index in 0..(arr.length() - 1)) {
                var country_name =  arr.getJSONObject(index).getString("country_name")
                var cases =  arr.getJSONObject(index).getString("cases")
                var new_cases =  arr.getJSONObject(index).getString("new_cases")
                var total_recovered =  arr.getJSONObject(index).getString("total_recovered")
                var deaths =  arr.getJSONObject(index).getString("deaths")
                var new_deaths =  arr.getJSONObject(index).getString("new_deaths")
                var total_cases_per_1m_population =  arr.getJSONObject(index).getString("total_cases_per_1m_population")
                val country = Country(0,country_name, cases, new_cases, total_recovered, deaths, new_deaths,total_cases_per_1m_population)
                //  println(country.country_name)
                ViewModel?.setMessage(country)

            }
        }
    }*/
}
