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

    fun getAllData() = countryDao?.getAll()


    fun search(countryname : String ) = countryDao?.search(countryname)


    fun setCountry(countryname: Country) {
        launch  { setCountryBG(countryname) }
    }
    private suspend fun setCountryBG(countryname: Country){
        withContext(Dispatchers.IO){
            countryDao?.addCountry(countryname)
        }
    }


    fun updateSubCountry(name: String?, sub: String)
    {
        launch  { updateSubCountryBG(name,sub) }
    }
    private suspend fun updateSubCountryBG(countryname: String?, sub: String){
        withContext(Dispatchers.IO){
            countryDao?.updateSubCountry(countryname  , sub)
        }
    }

    fun setALLCountry(country: ArrayList<Country>) {
        launch  { setALLCountryBG(country) }
    }
    private suspend fun setALLCountryBG(country: ArrayList<Country>){
        withContext(Dispatchers.IO){
            countryDao?.addALLCountry(country)
        }
    }


    fun delete() {
        launch  { deleteBG()}
    }

    private suspend fun deleteBG(){
        withContext(Dispatchers.IO){
            countryDao?.deleteAll()
        }
    }

    fun checkSubscribtion(country: Country) : Boolean
{
    var r : Boolean = false
    launch{
        r =  checkSubscribtionB(country)
    }
    return r
}
    private suspend fun checkSubscribtionB(country: Country)  : Boolean
    {
        var r : Boolean = false
        withContext(Dispatchers.IO){
            r = countryDao?.checkSub(country)!!
        }
        return r
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