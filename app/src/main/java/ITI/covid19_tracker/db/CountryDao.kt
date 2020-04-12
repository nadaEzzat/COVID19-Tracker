package ITI.covid19_tracker.db

import ITI.covid19_tracker.model.Country
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CountryDao {
//ORDER BY cases DESC

    @Query("SELECT * FROM Country_table ")
    fun getAll(): LiveData<List<Country>>

    @Query("SELECT * FROM Country_table WHERE country_name LIKE '%' || :name || '%'")
    fun search(name: String): LiveData<List<Country>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(country: Country): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateCountry(country: Country)

    @Query("DELETE FROM Country_table")
    fun deleteAll()

    @Query("UPDATE  Country_table SET Subscribe = :sub where country_name = :countryname")
    fun updateSubCountry(countryname: String?, sub: String)

    @Query("SELECT Subscribe from Country_table WHERE country_name = :countryname")
    fun getSubscribtion(countryname: String): String

    @Transaction
    fun addCountry(country: Country) {
        var flag: Long = insert(country)
        if (flag.equals(-1)) {

            //  checkSub(country.country_name)

            updateCountry(country)
        }
    }

    @Transaction
    fun checkSub(country: Country): Boolean {
        var flag: String = getSubscribtion(country.country_name)
        Log.i("tag", "SUBSCRIBTIO IN DAO : " + flag)
        if (flag == null) {
         //   Log.i("tag", "return null false")
            return false
        } else if (flag.equals("0")) {
           // Log.i("tag", "return 0 false")
            return false
        } else {
           checkIfChange(country)
            Log.i("tag", "return else true")
            return true
        }
    }

    @Transaction
    fun checkIfChange(country: Country)
    {
        var newDeath : String = getNewDeaths(country.country_name)

        var newCases : String = getNewCases(country.country_name)

        if(country.country_name.equals("EGYPT")) {
            if (newCases.equals(country.new_cases) || newDeath.equals(country.new_deaths)) {

                Log.i("tag", "FireNotification")
            }
        }
    }

    @Query("SELECT  new_deaths from Country_table WHERE country_name = :countryname")
    fun getNewDeaths(countryname: String): String

    @Query("SELECT new_cases from Country_table WHERE country_name = :countryname")
    fun getNewCases(countryname: String): String
/*
    @Query("SELECT Subscribe from Country_table WHERE country_name = :countryname")
    fun getSubscribtion(countryname: String): String

    @Query("SELECT Subscribe from Country_table WHERE country_name = :countryname")
    fun getSubscribtion(countryname: String): String

    @Query("SELECT Subscribe from Country_table WHERE country_name = :countryname")
    fun getSubscribtion(countryname: String): String
*/
}