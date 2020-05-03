package ITI.covid19_tracker.db

import ITI.covid19_tracker.NotificationHelper
import ITI.covid19_tracker.SplashScreen
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.view.MainActivity
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Maybe
import java.security.SecureRandom
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext


@Dao
interface CountryDao {

    @Query("SELECT * FROM Country_table ")
    fun getAll(): LiveData<List<Country>>

    @Query("SELECT * FROM Country_table WHERE country_name LIKE '%' || :name || '%'")
    fun search(name: String): LiveData<List<Country>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(country: Country): Long

    @Query("UPDATE  Country_table SET cases = :cases , new_cases=:newCases , deaths =:death , new_deaths=:newDeath , total_recovered=:totalRec , total_cases_per_1m_population= :total_cases_per_1m_population WHERE country_name = :countryname")
    fun updateCountry(
        cases: String,
        newCases: String,
        death: String,
        newDeath: String,
        total_cases_per_1m_population: String,
        totalRec: String,
        countryname: String
    ): Int

    @Query("DELETE FROM Country_table")
    fun deleteAll()

    //@Query("UPDATE  Country_table SET cases = :cases where country_name = :countryname")
    //fun updateCases(countryname: String?, cases: String)


    @Query("UPDATE  Country_table SET Subscribe = :sub where country_name = :countryname")
    fun updateSubCountry(countryname: String?, sub: String)

    @Query("SELECT Subscribe from Country_table WHERE country_name = :countryname")
    fun getSubscribtion(countryname: String): String

    @Transaction
    fun addALLCountry(country: ArrayList<Country>) {

        for (i in (1..country.size-1)) {

            var flag: Long = insert(country.get(i))

            if (flag == -1.toLong()) {
                checkSub(country.get(i))

                var ip: Int = updateCountry(
                    country.get(i).cases,
                    country.get(i).new_cases,
                    country.get(i).deaths,
                    country.get(i).new_deaths,
                    country.get(i).total_cases_per_1m_population,
                    country.get(i).total_recovered,
                    country.get(i).country_name
                )
                //    updateCases(country.country_name, country.cases)

            }
        }
    }


    @Transaction
    fun checkSub(country: Country): Boolean {
        var flag: String = getSubscribtion(country.country_name)
        if (flag == null) {
            return false
        } else if (flag.equals("0")) {
            return false
        } else {
            checkIfChange(country)
            return true
        }
    }

    @Transaction
    fun checkIfChange(country: Country) {
        var newDeath: String = getNewDeaths(country.country_name)

        var newCases: String = getNewCases(country.country_name)

        var Death: String = getDeaths(country.country_name)

        var Cases: String = getCases(country.country_name)


        if (newCases.equals(country.new_cases) || newDeath.equals(country.new_deaths) || !Cases.equals(country.cases) || !Death.equals(country.deaths)
        ) {
            var cases = "cases : " + Cases + " - > " + country.cases
            var dea = "Deaths : " + Death + " - > " + country.deaths
            Notif(country.country_name, cases, dea)

        }

    }

    @Query("SELECT  new_deaths from Country_table WHERE country_name = :countryname")
    fun getNewDeaths(countryname: String): String

    @Query("SELECT new_cases from Country_table WHERE country_name = :countryname")
    fun getNewCases(countryname: String): String

    @Query("SELECT cases from Country_table WHERE country_name = :countryname")
    fun getCases(countryname: String): String

    @Query("SELECT deaths from Country_table WHERE country_name = :countryname")
    fun getDeaths(countryname: String): String

    @Transaction
    fun Notif(countryname: String, cases: String, death: String) {

        val notificationHelper =
            NotificationHelper(
                SplashScreen.mContext,
                countryname,
                death
            )
        val nb: NotificationCompat.Builder? =
            notificationHelper.getChannelNotification()

        val notifyIntent: Intent = Intent()
        notifyIntent.setClassName(
            "ITI.covid19_tracker",
            "ITI.covid19_tracker.view.Tab.ActivityTab"
        )
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val notifyPendingIntent = PendingIntent.getActivity(
            SplashScreen.mContext,
            notifyIntent.getIntExtra("request", 0),
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (nb != null) {
            nb.setContentIntent(notifyPendingIntent)
        }
        notificationHelper.getManager()
            .notify(Intent().getIntExtra("request", createRandomCode(7)), nb?.build())


    }

    @Transaction
    fun createRandomCode(codeLength: Int): Int {
        val chars = "1234567890".toCharArray()
        val sb = StringBuilder()
        val random: Random = SecureRandom()
        for (i in 0 until codeLength) {
            val c = chars[random.nextInt(chars.size)]
            sb.append(c)
        }
        return sb.toString().toInt()
    }


    /*
    @Transaction
    fun addCountry(country: Country) {
        var flag: Long = insert(country)

        if (flag == -1.toLong()) {
            checkSub(country)
           var ip: Int = updateCountry(
                country.cases,
                country.new_cases,
                country.deaths,
                country.new_deaths,
                country.total_cases_per_1m_population,
                country.total_recovered,
                country.country_name
            )
        //    updateCases(country.country_name, country.cases)
          }
    }
*/
}