package ITI.covid19_tracker.model

import com.google.gson.annotations.SerializedName

class HistoryModel {
    @SerializedName("stat_by_country")
    var stat_by_country: Array<history_data>?  = null

    @SerializedName("country")
    var country: String = ""

}

class history_data {
    var country_name: String = ""
    var total_cases: String = ""
    var record_date:String = ""
    }