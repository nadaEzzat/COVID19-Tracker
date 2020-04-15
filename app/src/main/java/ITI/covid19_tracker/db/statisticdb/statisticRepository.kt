package ITI.covid19_tracker.db.statisticdb

import ITI.covid19_tracker.db.CountryDao
import ITI.covid19_tracker.db.CountryDatabase
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.statisticModel
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class statisticRepository(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var statisticDao: statisticDao

    init {
        val db = CountryDatabase.getDatabase(application)
        statisticDao = db?.statisticDao()!!
    }

    fun getAllData() = statisticDao?.getAll()

    fun setCountry(static: statisticModel) {
        launch  { setCountryBG(static) }
    }
    private suspend fun setCountryBG(static: statisticModel){
        withContext(Dispatchers.IO){
            statisticDao?.set(static)
        }
    }

    fun delete() {
        launch  { deleteBG()}
    }

    private suspend fun deleteBG(){
        withContext(Dispatchers.IO){
            statisticDao?.deleteAll()
        }
    }

    /*{
        countryDao?.search(countryname)
        //launch  { searchBG(countryname)}
    }



    private suspend fun searchBG(countryname : String){
        withContext(Dispatchers.IO){
            countryDao?.search(countryname)
        }
    }
*/
}