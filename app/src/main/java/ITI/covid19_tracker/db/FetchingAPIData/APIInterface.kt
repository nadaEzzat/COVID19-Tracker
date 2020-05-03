package ITI.covid19_tracker.db.FetchingAPIData

import ITI.covid19_tracker.model.Model
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("cases_by_country.php")
    fun getAllData(): Observable<Model>
}