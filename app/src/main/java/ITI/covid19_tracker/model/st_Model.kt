package ITI.covid19_tracker.model

import com.google.gson.annotations.SerializedName


class st_Model {
    @SerializedName("total_cases")
    var total_cases: String = ""

    @SerializedName("total_deaths")
    var total_deaths: String = ""

    @SerializedName("total_recovered")
    var total_recovered: String = ""

    @SerializedName("new_cases")
    var new_cases: String = ""

    @SerializedName("new_deaths")
    var new_deaths: String = ""

    @SerializedName("statistic_taken_at")
    var statistic_taken_at: String = ""

}