package ITI.covid19_tracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//change in confirmed, recovered or death cases
//{"country_name":"USA","cases":"332,993","deaths":"9,528","region":"","total_recovered":"17,018","new_deaths":"1,076","new_cases":"21,636","serious_critical":"8,623","active_cases":"306,447","total_cases_per_1m_population":"1,006"}
@Entity(tableName = "Country_table")
class Country(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "country_name") var country_name: String,
    @ColumnInfo(name = "cases") var cases: String,
    @ColumnInfo(name = "new_cases") var new_cases: String,
    @ColumnInfo(name = "total_recovered") var total_recovered: String,
    @ColumnInfo(name = "deaths") var deaths: String,
    @ColumnInfo(name = "new_deaths") var new_deaths: String,
    @ColumnInfo(name = "total_cases_per_1m_population") var total_cases_per_1m_population: String
)//{
// country_name: String, cases: String, deaths: String, total_recovered: String, new_cases: String){

/*
    init {
        this.country_name = country_name
        this.cases = cases
        this.total_recovered = total_recovered
        this.deaths = deaths
        this.new_cases = new_cases
    }

 */
//}