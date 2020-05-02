package ITI.covid19_tracker.viewmodel

import ITI.covid19_tracker.db.CountryRepository
import ITI.covid19_tracker.model.Country
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private var repository:CountryRepository = CountryRepository(application)

    fun getAllData() = repository.getAllData()

    fun setCountry(country: Country) { repository.setCountry(country)}

    fun setALLCountry(country: ArrayList<Country>) { repository.setALLCountry(country)}

    fun delete() { repository.delete()}

    fun search(countryname : String) = repository.search(countryname)

    fun updateSubCountry(countryname: String?, sub: String) = repository.updateSubCountry(countryname ,sub)

    fun checkSubscribtion(country: Country) :Boolean {
        return  repository.checkSubscribtion(country)
    }
}