package ITI.covid19_tracker.FetchingAPIData

import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.Model
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("cases_by_country.php")
    fun getAllData(): Call<Model>
}