package ITI.covid19_tracker.db.FetchingAPIData.StatisticData

import ITI.covid19_tracker.model.Model
import ITI.covid19_tracker.model.st_Model
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

interface APIStatistic {

    @GET("worldstat.php")
    fun getAllData(): Observable<st_Model>
}