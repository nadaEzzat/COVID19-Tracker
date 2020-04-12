package ITI.covid19_tracker.model

import com.google.gson.annotations.SerializedName

class Model {
    @SerializedName("countries_stat")
    var countries_stat: Array<countries_stat_data>?  = null

    @SerializedName("statistic_taken_at")
    var statistic_taken_at: String = ""

}

class countries_stat_data {
    var country_name: String = ""
    var cases: String = ""
    var deaths: String = ""
    var region: String = ""
    var total_recovered: String = ""
    var new_deaths: String = ""
    var new_cases: String = ""
    var serious_critical: String = ""
    var active_cases: String = ""
    var total_cases_per_1m_population: String = ""
}