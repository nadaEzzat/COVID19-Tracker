package ITI.covid19_tracker.viewmodel

import ITI.covid19_tracker.db.CountryRepository
import ITI.covid19_tracker.db.statisticdb.statisticRepository
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.model.statisticModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class StatisticViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: statisticRepository = statisticRepository(application)

    fun getAllData() = repository.getAllData()

    fun setCountry(statisticModel: statisticModel) { repository.setCountry(statisticModel)}

    fun delete() { repository.delete()}


}