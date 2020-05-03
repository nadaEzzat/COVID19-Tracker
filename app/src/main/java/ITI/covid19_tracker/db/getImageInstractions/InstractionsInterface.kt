package ITI.covid19_tracker.db.getImageInstractions

import ITI.covid19_tracker.model.Model
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

interface InstractionsInterface {
    @GET("random_masks_usage_instructions.php")
    fun getImage(): Observable<ResponseBody>
}
