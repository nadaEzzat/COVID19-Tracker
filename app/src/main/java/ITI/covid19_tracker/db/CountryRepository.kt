package ITI.covid19_tracker.db

import ITI.covid19_tracker.model.Country
import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class CountryRepository(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var countryDao: CountryDao?

    init {
        val db = CountryDatabase.getDatabase(application)
        countryDao = db?.countryDao()
    }

    fun getMessages() = countryDao?.getAll()

    fun search(countryname : String ) = countryDao?.search(countryname)

    fun setMessage(message: Country) {
        launch  { setMessageBG(message) }
    }

    fun delete() {
        launch  { deleteBG()}
    }

    private suspend fun setMessageBG(message: Country){
        withContext(Dispatchers.IO){
            countryDao?.insert(message)
        }
    }
    private suspend fun deleteBG(){
        withContext(Dispatchers.IO){
            countryDao?.deleteAll()
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
/*
interface CountryRepository {
    fun getSavedCountries() : LiveData<List<Country>>
    fun saveCounry(country: Country)
    fun deleteCountry(country: Country)
    fun searchCountry(name : String) : LiveData<List<Country>>
}

 */