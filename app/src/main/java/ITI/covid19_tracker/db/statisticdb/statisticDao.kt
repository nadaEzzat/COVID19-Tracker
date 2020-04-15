package ITI.covid19_tracker.db.statisticdb

import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.statisticModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface statisticDao {
    @Query("SELECT * FROM Statistic_table ")
    fun getAll(): LiveData<List<statisticModel>>

    @Query("DELETE FROM Statistic_table")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(country: statisticModel): Long

    @Transaction
    fun set(country: statisticModel)
    {
        var l : Long = insert(country)
        Log.i("tag","DDDDDDDDDDDDDDDDDDDDONNNNNNNNNNNNNNNNNNNNNNNNNNNNE   + "+l)
    }
}