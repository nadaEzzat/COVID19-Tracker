package ITI.covid19_tracker.db.historyAPI

import ITI.covid19_tracker.model.HistoryModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface HistoryInterface {

    @GET("cases_by_particular_country.php?/")
    fun getHistoryData(@QueryMap country_name: MutableMap<String, String>) :Observable<HistoryModel>

}