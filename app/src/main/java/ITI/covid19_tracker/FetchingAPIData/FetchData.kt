package ITI.covid19_tracker.FetchingAPIData

import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class FetchData : AppCompatActivity(), FetchCompleteListener {

    private lateinit var CountryDetailsJSON: JSONObject

    private var messageViewModel: MainViewModel? = null



    fun getDetails(message: MainViewModel? ) {
        messageViewModel = message

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
            messageViewModel?.delete()
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
                messageViewModel?.setMessage(country)

            }
            /*
             @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "country_name") var country_name: String,
    @ColumnInfo(name = "cases") var cases: String,
    @ColumnInfo(name = "new_cases") var new_cases: String,
    @ColumnInfo(name = "total_recovered") var total_recovered: String,
    @ColumnInfo(name = "deaths") var deaths: String,
    @ColumnInfo(name = "new_deaths") var new_deaths: String
             */
            //CountryDetailsJSON.optJSONArray("countries_stat")
/*
            val data = CountryDetailsJSON.optJSONArray("data")
                ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
                ?.map { Foo(it.toString()) } // transforms each JSONObject of the array into Foo
*/
            //Log.i("tag", "statistic_taken_at  :  " +CountryDetailsJSON.getString("statistic_taken_at"))
            //Log.i("tag", CountryDetailsJSON.getString("countries_stat"))

        }
    }
}
/*
class Foo(json: String) : JSONObject(json) {

    val id = this.optString("id")
    val title: String? = this.optString("title")
}

 */