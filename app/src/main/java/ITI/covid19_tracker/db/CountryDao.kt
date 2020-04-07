package ITI.covid19_tracker.db

import ITI.covid19_tracker.model.Country
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import retrofit2.http.DELETE


@Dao
interface CountryDao {
//ORDER BY cases DESC

    @Query("SELECT * FROM Country_table ")
    fun getAll(): LiveData<List<Country>>

    @Query("SELECT * FROM Country_table WHERE country_name LIKE '%' || :name || '%'")
    fun search(name : String): LiveData<List<Country>>

    @Insert(onConflict = REPLACE)
    fun insert(country: Country)

    @Update
    fun updateCountry(country: Country)

    @Query("DELETE FROM Country_table")
    fun deleteAll()




}