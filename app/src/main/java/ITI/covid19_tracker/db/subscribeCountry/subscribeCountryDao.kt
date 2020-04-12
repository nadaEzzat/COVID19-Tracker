package ITI.covid19_tracker.db.subscribeCountry


import ITI.covid19_tracker.model.subscribeCountry
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface subscribeCountryDao {

    @Insert(onConflict = REPLACE)
    fun setSubscribeCountry(country: subscribeCountry)

    @Delete
    fun deleteSubscribeCountry(country : subscribeCountry)

    @Query("SELECT * FROM subscrib_table WHERE country_name  = :name")
    fun search(name : String): LiveData<List<subscribeCountry>>

}