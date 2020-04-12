package ITI.covid19_tracker.db.subscribeCountry

import ITI.covid19_tracker.db.CountryDao
import ITI.covid19_tracker.db.CountryDatabase
import ITI.covid19_tracker.model.subscribeCountry
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class subscribeCountryRepository (application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var subscribeCountryDao: subscribeCountryDao?

    init {
        val db = CountryDatabase.getDatabase(application)
        subscribeCountryDao = db?.subscribeCountryDao()
    }

    fun addSubscribCountries(country: subscribeCountry) = subscribeCountryDao?.setSubscribeCountry(country)

    fun searchSubscribCountries(country: String) = subscribeCountryDao?.search(country)

    fun deleteSubscribCountries(country: subscribeCountry) = subscribeCountryDao?.deleteSubscribeCountry(country)

}