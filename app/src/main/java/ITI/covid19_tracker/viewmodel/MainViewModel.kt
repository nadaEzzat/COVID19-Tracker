package ITI.covid19_tracker.viewmodel

import ITI.covid19_tracker.db.CountryRepository
import ITI.covid19_tracker.model.Country
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private var repository:CountryRepository = CountryRepository(application)

    fun getMessages() = repository.getMessages()

    fun setMessage(message: Country) { repository.setMessage(message)}

    fun delete() { repository.delete()}

    fun search(countryname : String) = repository.search(countryname)
}