package ITI.covid19_tracker.db.getImageInstractions

import ITI.covid19_tracker.model.Model
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface InstractionsInterface {
    @GET("random_masks_usage_instructions.php")
    fun getImage(): Call<ResponseBody>
}
