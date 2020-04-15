package ITI.covid19_tracker.db.FetchingAPIData.StatisticData

import ITI.covid19_tracker.model.Model
import ITI.covid19_tracker.model.st_Model
import retrofit2.Call
import retrofit2.http.GET

interface APIStatistic {

    @GET("worldstat.php")
    fun getAllData(): Call<st_Model>
}